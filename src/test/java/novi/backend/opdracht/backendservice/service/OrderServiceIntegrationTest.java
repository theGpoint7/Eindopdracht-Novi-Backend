package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.OrderRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDTO;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        // Create and save a test user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("securepassword");
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEnabledAccount(true);
        user.setAddress("123 Test Street");
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testPlaceOrder_Success() {
        User user = userRepository.findByUsername("testuser").orElseThrow();

        // Create and save the cart for the user
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        // Create and save a product to be added to the cart
        Clothing product = new Clothing();
        product.setProductName("Test Product");
        product.setProductType("Clothing");
        product.setPrice(50.0);
        product.setInventoryCount(10);
        product.setDesigner(designerRepository.findById(1L).orElseThrow());
        productRepository.save(product);

        // Add the product to the cart
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.getItems().add(cartItem);
        cartRepository.save(cart);

        // Create an order request
        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setRetrieveCartItems(true);

        // Place the order
        orderService.placeOrder(orderRequest);

        // Verify the order is saved
        List<Order> orders = orderRepository.findByUserUsername("testuser");
        assertEquals(1, orders.size());

        Order order = orders.get(0);
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
        assertEquals(100.0, order.getAmount());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testPlaceOrder_ValidationErrors() {
        // Ensure the cart is empty for the user
        User user = userRepository.findByUsername("testuser").orElseThrow();
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        // Try to place an order with an empty cart
        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setRetrieveCartItems(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            orderService.placeOrder(orderRequest);
        });

        assertEquals("Kan geen bestelling plaatsen met een lege winkelwagen", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testCancelOrder_NotFound() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.cancelOrder(999L);
        });

        assertEquals("Bestelling niet gevonden", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testCancelOrder_AuthorizationErrors() {
        User user = userRepository.findByUsername("testuser").orElseThrow();

        // Create and save an order for another user
        User anotherUser = new User();
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword("securepassword");
        anotherUser.setEmail("anotheruser@example.com");
        anotherUser.setFirstName("Another");
        anotherUser.setLastName("User");
        anotherUser.setEnabledAccount(true);
        anotherUser.setAddress("456 Another Street");
        userRepository.save(anotherUser);

        Order order = new Order();
        order.setUser(anotherUser);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(anotherUser.getAddress());
        orderRepository.save(order);

        AuthorizationServiceException exception = assertThrows(AuthorizationServiceException.class, () -> {
            orderService.cancelOrder(order.getOrderId());
        });

        assertEquals("Je bent niet gemachtigd om deze bestelling te annuleren", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_OtherErrors() {
        User user = userRepository.findByUsername("testuser").orElseThrow();

        // Create and save an order with payment not confirmed
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        orderRepository.save(order);

        // Try to confirm shipment
        ResponseEntity<String> response = orderService.confirmShipment(order.getOrderId());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Betaling is niet bevestigd", response.getBody());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_AlreadyShipped() {
        User user = userRepository.findByUsername("testuser").orElseThrow();

        // Create and save an order with status SHIPPED
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setPaymentStatus(PaymentStatus.CONFIRMED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        orderRepository.save(order);

        // Try to confirm shipment
        ResponseEntity<String> response = orderService.confirmShipment(order.getOrderId());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Bestelling is al verzonden", response.getBody());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetReceiptForOrder_Success() {
        User user = userRepository.findByUsername("testuser").orElseThrow();

        // Create and save an order
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setPaymentStatus(PaymentStatus.CONFIRMED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        orderRepository.save(order);

        // Generate a receipt
        Receipt receipt = new Receipt();
        receipt.setDateIssued(LocalDateTime.now());
        receipt.setTotalAmount(100.0);
        receipt.setShippingCost(10.0);
        order.setReceipt(receipt);
        orderRepository.save(order);

        // Retrieve the receipt
        ReceiptOutputDTO receiptOutputDTO = orderService.getReceiptForOrder(order.getOrderId());

        assertNotNull(receiptOutputDTO);
        assertEquals(100.0, receiptOutputDTO.getTotalAmount());
        assertEquals(10.0, receiptOutputDTO.getShippingCost());
    }
}
