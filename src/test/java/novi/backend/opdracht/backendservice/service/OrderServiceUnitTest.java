package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.CartItemInputDto;
import novi.backend.opdracht.backendservice.dto.input.OrderRequestDto;
import novi.backend.opdracht.backendservice.dto.output.OrderOutputDto;
import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private User anotherUser;
    private Cart cart;
    private AbstractProduct product;
    private Designer designer;
    private CartItem cartItem;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setAddress("123 Test Street");

        anotherUser = new User();
        anotherUser.setUsername("anotheruser");
        anotherUser.setAddress("456 Another Street");

        designer = new Designer();
        designer.setDesignerId(1L);
        designer.setUser(user);

        promotion = new Promotion();
        promotion.setPromotionPercentage(20.0);

        product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setInventoryCount(100);
        product.setPrice(10.0);
        product.setDesigner(designer);
        product.setPromotion(promotion);

        cartItem = new CartItem(cart, product, 2);

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(Collections.singletonList(cartItem)));
    }

    @Test
    void testPlaceOrder_Success() {
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(Collections.singletonList(new CartItemInputDto(product.getProductId(), cartItem.getQuantity())));

        assertDoesNotThrow(() -> orderService.placeOrder(orderRequestDTO));

        Mockito.verify(orderRepository).save(any(Order.class));
        Mockito.verify(cartService).saveCart(any(Cart.class));
    }

    @Test
    void testPlaceOrder_EmptyCart() {
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        cart.setItems(Collections.emptyList());
        Mockito.when(cartService.getUserCart()).thenReturn(cart);

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> orderService.placeOrder(orderRequestDTO));
        assertEquals("Kan geen bestelling plaatsen met een lege winkelwagen", exception.getMessage());
    }

    @Test
    void testPlaceOrder_InsufficientInventory() {
        product.setInventoryCount(1);
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(Collections.singletonList(new CartItemInputDto(product.getProductId(), cartItem.getQuantity())));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> orderService.placeOrder(orderRequestDTO));
        assertEquals("Onvoldoende voorraad voor product: Test Product", exception.getMessage());
    }

    @Test
    void testPlaceOrder_ProductNotFound() {
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(Collections.singletonList(new CartItemInputDto(product.getProductId(), cartItem.getQuantity())));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(orderRequestDTO));
        assertEquals("Product niet gevonden", exception.getMessage());

        Mockito.verify(orderRepository, Mockito.never()).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_ProductNull() {
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(List.of(new CartItemInputDto(product.getProductId(), cartItem.getQuantity())));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(orderRequestDTO));
        assertEquals("Product niet gevonden", exception.getMessage());

        Mockito.verify(orderRepository, Mockito.never()).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_MultipleDesigners() {
        AbstractProduct product2 = new Clothing();
        product2.setProductId(2L);
        product2.setProductName("Another Product");
        product2.setInventoryCount(50);
        product2.setPrice(15.0);
        Designer anotherDesigner = new Designer();
        anotherDesigner.setDesignerId(2L);
        product2.setDesigner(anotherDesigner);

        cart.getItems().add(new CartItem(cart, product2, 1));

        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(List.of(
                new CartItemInputDto(product.getProductId(), cartItem.getQuantity()),
                new CartItemInputDto(product2.getProductId(), 1)
        ));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> orderService.placeOrder(orderRequestDTO));
        assertEquals("Alle producten in de bestelling moeten van dezelfde ontwerper zijn", exception.getMessage());

        Mockito.verify(orderRepository, Mockito.never()).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_SameDesigner() {
        AbstractProduct product2 = new Clothing();
        product2.setProductId(2L);
        product2.setProductName("Another Product");
        product2.setInventoryCount(50);
        product2.setPrice(15.0);
        product2.setDesigner(designer);

        cart.getItems().add(new CartItem(cart, product2, 1));

        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(List.of(
                new CartItemInputDto(product.getProductId(), cartItem.getQuantity()),
                new CartItemInputDto(product2.getProductId(), 1)
        ));

        assertDoesNotThrow(() -> orderService.placeOrder(orderRequestDTO));

        Mockito.verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_WithCartItemsNotRetrieved() {
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);
        Mockito.when(cartService.getUserCart()).thenReturn(cart);
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(false);
        orderRequestDTO.setCartItems(Collections.singletonList(new CartItemInputDto(product.getProductId(), cartItem.getQuantity())));

        assertDoesNotThrow(() -> orderService.placeOrder(orderRequestDTO));

        Mockito.verify(orderRepository).save(any(Order.class));
        Mockito.verify(cartService).saveCart(any(Cart.class));
    }

    @Test
    void testGetOrderDetails_NotFound() {
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderDetails(1L));
        assertEquals("Bestelling niet gevonden", exception.getMessage());
    }

    @Test
    void testGetOrderDetails_NotAuthorized() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(anotherUser);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(anotherUser.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        AuthorizationServiceException exception = assertThrows(AuthorizationServiceException.class, () -> orderService.getOrderDetails(1L));
        assertEquals("Je bent niet gemachtigd om deze bestelling te bekijken", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetOrderDetails_Success() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        OrderOutputDto result = orderService.getOrderDetails(1L);

        assertEquals(1L, result.getOrderId());
        assertEquals("testuser", result.getUsername());
        assertEquals(100.0, result.getAmount());
    }

    @Test
    void testGetUserOrders_Success() {
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        Order order1 = new Order();
        order1.setOrderId(1L);
        order1.setUser(user);
        order1.setOrderStatus(OrderStatus.PENDING);
        order1.setAmount(100.0);
        order1.setOrderDateTime(LocalDateTime.now());
        order1.setShippingAddress(user.getAddress());

        Order order2 = new Order();
        order2.setOrderId(2L);
        order2.setUser(user);
        order2.setOrderStatus(OrderStatus.SHIPPED);
        order2.setAmount(200.0);
        order2.setOrderDateTime(LocalDateTime.now().minusDays(1));
        order2.setShippingAddress(user.getAddress());

        List<Order> userOrders = List.of(order1, order2);
        Mockito.when(orderRepository.findByUserUsername(user.getUsername())).thenReturn(userOrders);

        List<OrderOutputDto> result = orderService.getUserOrders();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderId());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals(100.0, result.get(0).getAmount());
        assertEquals(OrderStatus.PENDING, result.get(0).getOrderStatus());

        assertEquals(2L, result.get(1).getOrderId());
        assertEquals("testuser", result.get(1).getUsername());
        assertEquals(200.0, result.get(1).getAmount());
        assertEquals(OrderStatus.SHIPPED, result.get(1).getOrderStatus());
    }

    @Test
    void testGetOrderDetails_WithPaymentMethod() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        AbstractPaymentMethod paymentMethod = new CreditCardPayment();
        ((CreditCardPayment) paymentMethod).setCardNumber("1234567890123456");
        ((CreditCardPayment) paymentMethod).setExpiryDate("12/24");
        ((CreditCardPayment) paymentMethod).setCvv("123");
        order.setPaymentMethod(paymentMethod);

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        OrderOutputDto result = orderService.getOrderDetails(1L);

        assertEquals(1L, result.getOrderId());
        assertEquals("testuser", result.getUsername());
        assertEquals(100.0, result.getAmount());
        assertEquals("CREDIT_CARD", result.getPaymentMethodType());
    }

    @Test
    void testGetOrderDetails_WithoutPaymentMethod() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        order.setPaymentMethod(null);

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        OrderOutputDto result = orderService.getOrderDetails(1L);

        assertEquals(1L, result.getOrderId());
        assertEquals("testuser", result.getUsername());
        assertEquals(100.0, result.getAmount());
        assertNull(result.getPaymentMethodType());
    }

    @Test
    void testCancelOrder_NotAuthorized() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(anotherUser);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(anotherUser.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        AuthorizationServiceException exception = assertThrows(AuthorizationServiceException.class, () -> orderService.cancelOrder(1L));
        assertEquals("Je bent niet gemachtigd om deze bestelling te annuleren", exception.getMessage());
    }

    @Test
    void testCancelOrder_NotFound() {
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder(1L));
        assertEquals("Bestelling niet gevonden", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testCancelOrder_Success() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        assertDoesNotThrow(() -> orderService.cancelOrder(1L));

        Mockito.verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testConfirmShipment_NotFound() {
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = orderService.confirmShipment(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Bestelling niet gevonden", response.getBody());
    }

    @Test
    void testConfirmShipment_AlreadyShipped() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setPaymentStatus(PaymentStatus.CONFIRMED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ResponseEntity<String> response = orderService.confirmShipment(1L);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Bestelling is al verzonden", response.getBody());
    }

    @Test
    void testConfirmShipment_PaymentNotConfirmed() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ResponseEntity<String> response = orderService.confirmShipment(1L);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Betaling is niet bevestigd", response.getBody());
    }

    @Test
    void testConfirmShipment_OrderNotContainsProductsByDesigner() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.CONFIRMED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        AbstractProduct product = new Clothing();
        Designer anotherDesigner = new Designer();
        anotherDesigner.setDesignerId(2L);
        anotherDesigner.setUser(anotherUser);
        product.setDesigner(anotherDesigner);

        OrderLine orderLine = new OrderLine(order, product, 1, 10.0, 0.0);
        order.addOrderLine(orderLine);

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ResponseEntity<String> response = orderService.confirmShipment(1L);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("De bestelling bevat geen producten van de ontwerper die de verzending bevestigt", response.getBody());
    }

    @Test
    void testConfirmShipment_IllegalStateException() {
        Mockito.when(orderRepository.findById(anyLong())).thenThrow(new IllegalStateException("Illegal state"));

        ResponseEntity<String> response = orderService.confirmShipment(1L);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Illegal state", response.getBody());
    }

    @Test
    void testConfirmShipment_InternalServerError() {
        Mockito.when(orderRepository.findById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<String> response = orderService.confirmShipment(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Er is een fout opgetreden bij het bevestigen van de verzending.", response.getBody());
    }

    @Test
    void testConfirmShipment_Success() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.CONFIRMED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        AbstractProduct product = new Clothing();
        product.setDesigner(designer);

        OrderLine orderLine = new OrderLine(order, product, 1, 10.0, 0.0);
        order.addOrderLine(orderLine);

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ResponseEntity<String> response = orderService.confirmShipment(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verzending bevestigd voor bestelling: 1", response.getBody());

        Mockito.verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testGetReceiptForOrder_NotFound() {
        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getReceiptForOrder(1L));
        assertEquals("Bestelling niet gevonden", exception.getMessage());
    }

    @Test
    void testGetReceiptForOrder_NotAuthorized() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(anotherUser);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(anotherUser.getAddress());

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        AuthorizationServiceException exception = assertThrows(AuthorizationServiceException.class, () -> orderService.getReceiptForOrder(1L));
        assertEquals("Je bent niet gemachtigd om deze bon te bekijken", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetReceiptForOrder_ReceiptNotFound() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        order.setReceipt(null);

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getReceiptForOrder(1L));
        assertEquals("Bon niet gevonden voor bestelling met ID: 1", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetReceiptForOrder_Success() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        Receipt receipt = new Receipt();
        receipt.setReceiptId(1L);
        receipt.setDateIssued(LocalDateTime.now());
        receipt.setTotalAmount(100.0);
        receipt.setShippingCost(0.0);
        order.setReceipt(receipt);

        Mockito.when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        Mockito.when(authenticationService.getCurrentUser()).thenReturn(user);

        ReceiptOutputDto result = orderService.getReceiptForOrder(1L);

        assertEquals(1L, result.getReceiptId());
        assertEquals(100.0, result.getTotalAmount());
        assertEquals(0.0, result.getShippingCost());
    }

    @Test
    void testCalculateDiscountAmount_NoPromotion() {
        product.setPromotion(null);

        double discountAmount = orderService.calculateDiscountAmount(product, 2);
        assertEquals(0.0, discountAmount);
    }

    @Test
    void testCalculateDiscountAmount_WithPromotion() {
        double discountAmount = orderService.calculateDiscountAmount(product, 2);
        assertEquals(4.0, discountAmount);
    }
}
