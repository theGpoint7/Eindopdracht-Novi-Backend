package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.OrderDto;
import novi.backend.opdracht.backendservice.dto.OrderLineDto;
import novi.backend.opdracht.backendservice.dto.ProductDto;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.CartRepository;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import novi.backend.opdracht.backendservice.repository.PaymentMethodRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import novi.backend.opdracht.backendservice.service.EmailService;
import novi.backend.opdracht.backendservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/createFromCart")
    public ResponseEntity<?> createOrderFromCart(@RequestBody OrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        // Retrieve cart for the user
        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty. Cannot create order.");
        }

        List<CartItem> cartItems = cart.getItems();

        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal shippingCosts = orderService.calculateShippingCosts(cartItems);

        BigDecimal total = subtotal.add(shippingCosts);

        // Get the paymentMethodName from the DTO
        String paymentMethodName = orderDto.getPaymentMethod();
        if (paymentMethodName == null || paymentMethodName.isEmpty()) {
            return ResponseEntity.badRequest().body("Payment method name is required.");
        }

        // Check if the provided payment method exists in the database
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findByMethodName(paymentMethodName);
        if (optionalPaymentMethod.isEmpty()) {
            return ResponseEntity.badRequest().body("Payment method not found with name: " + paymentMethodName);
        }

        PaymentMethod paymentMethod = optionalPaymentMethod.get();

        String customDeliveryAddress = orderDto.getDeliveryAddress();

        // Set delivery address based on custom or user's default address
        String deliveryAddress = customDeliveryAddress != null && !customDeliveryAddress.isEmpty()
                ? customDeliveryAddress
                : user.getUserProfile().getAddress();

        // Convert cart items to OrderLineDto objects and add to OrderDto
        List<OrderLineDto> orderLines = cartItems.stream()
                .map(cartItem -> {
                    OrderLineDto orderLineDto = new OrderLineDto();
                    ProductDto productDto = new ProductDto();
                    Product cartProduct = cartItem.getProduct(); // Get the product from cartItem
                    productDto.setId(cartProduct.getId());
                    productDto.setName(cartProduct.getName());
                    productDto.setDescription(cartProduct.getDescription()); // Set description from cartProduct
                    productDto.setPrice(cartProduct.getPrice());
                    productDto.setInventoryCount(cartProduct.getInventoryCount());
                    productDto.setImageUrl(cartProduct.getImageUrl());
                    orderLineDto.setProduct(productDto);
                    orderLineDto.setQuantity(cartItem.getQuantity());
                    return orderLineDto;
                })
                .collect(Collectors.toList());

        // Set the order lines in the OrderDto
        orderDto.setOrderLines(orderLines);

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setOrderLines(orderDto.getOrderLines().stream()
                .map(orderLineDto -> {
                    OrderLine orderLine = new OrderLine();
                    ProductDto productDto = orderLineDto.getProduct();
                    // Here we fetch the product from the repository based on the ID
                    Product product = productRepository.findById(productDto.getId()).orElse(null);
                    if (product == null) {
                        // Handle product not found error
                        throw new RuntimeException("Product not found with ID: " + productDto.getId());
                    }
                    orderLine.setProduct(product);
                    orderLine.setQuantity(orderLineDto.getQuantity());
                    orderLine.setUnitPrice(product.getPrice()); // Set unit price based on product price
                    orderLine.setOrder(order);
                    // Set other order line details
                    return orderLine;
                })
                .collect(Collectors.toList()));
        order.setSubtotal(subtotal);
        order.setShippingCosts(shippingCosts);
        order.setPaymentStatus(PaymentStatus.PENDING); // Or set the appropriate status
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING); // Set status as "Pending" for the new order
        order.setDeliveryAddress(deliveryAddress); // Set delivery address

        // Log the delivery address to ensure it's correct
        System.out.println("Delivery Address: " + deliveryAddress);

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // Convert the savedOrder to DTO
        OrderDto savedOrderDto = convertToDto(savedOrder);

        return ResponseEntity.ok().body(savedOrderDto);
    }

    // Helper method to calculate subtotal
    private BigDecimal calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Convert to DTO
    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());

        // Set the order date to current timestamp if it's null
        if (orderDto.getOrderDate() == null) {
            orderDto.setOrderDate(LocalDateTime.now());
        }

        orderDto.setUser(order.getUser().getUserProfile().getFirstName() + " " +
                order.getUser().getUserProfile().getLastName());
        orderDto.setDeliveryAddress(order.getDeliveryAddress()); // Use delivery address
        orderDto.setOrderLines(order.getOrderLines().stream()
                .map(this::convertOrderLineToDto)
                .collect(Collectors.toList()));
        orderDto.setSubtotal(order.getSubtotal());
        orderDto.setShippingCosts(order.getShippingCosts());
        orderDto.setTotal(order.getTotal());
        orderDto.setPaymentMethod(order.getPaymentMethod().getMethodName());

        return orderDto;
    }

    private OrderLineDto convertOrderLineToDto(OrderLine orderLine) {
        OrderLineDto orderLineDto = new OrderLineDto();
        ProductDto productDto = new ProductDto();
        productDto.setId(orderLine.getProduct().getId());
        productDto.setName(orderLine.getProduct().getName());
        productDto.setDescription(orderLine.getProduct().getDescription());
        productDto.setPrice(orderLine.getProduct().getPrice());
        productDto.setInventoryCount(orderLine.getProduct().getInventoryCount());
        productDto.setImageUrl(orderLine.getProduct().getImageUrl());
        orderLineDto.setProduct(productDto);
        orderLineDto.setQuantity(orderLine.getQuantity());
        return orderLineDto;
    }

    // Getters and Setters
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
