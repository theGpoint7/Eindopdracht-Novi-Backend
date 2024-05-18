package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.CartItemInputDTO;
import novi.backend.opdracht.backendservice.dto.input.OrderRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.OrderOutputDTO;
import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDTO;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductRepository productRepository, AuthenticationService authenticationService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.authenticationService = authenticationService;
    }

    public void placeOrder(OrderRequestDTO orderRequest) {
        User user = authenticationService.getCurrentUser();
        Cart cart = cartService.getUserCart();
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Kan geen bestelling plaatsen met een lege winkelwagen");
        }

        List<CartItemInputDTO> cartItems;
        if (orderRequest.isRetrieveCartItems()) {
            cartItems = cart.getItems().stream()
                    .map(cartItem -> new CartItemInputDTO(cartItem.getProduct().getProductId(), cartItem.getQuantity()))
                    .collect(Collectors.toList());
        } else {
            cartItems = orderRequest.getCartItems();
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        double totalPrice = 0.0;
        Long commonDesignerId = null;
        for (CartItemInputDTO cartItem : cartItems) {
            AbstractProduct product = getProductById(cartItem.getProductId());
            if (product.getInventoryCount() < cartItem.getQuantity()) {
                throw new BadRequestException("Onvoldoende voorraad voor product: " + product.getProductName());
            }
            product.setInventoryCount(product.getInventoryCount() - cartItem.getQuantity());
            productRepository.save(product);

            double discountAmount = calculateDiscountAmount(product, cartItem.getQuantity());
            double productPrice = (product.getPrice() - discountAmount) * cartItem.getQuantity();
            totalPrice += productPrice;

            Long designerId = product.getDesigner().getDesignerId();

            OrderLine orderLine = new OrderLine(order, product, cartItem.getQuantity(), productPrice, discountAmount);
            order.addOrderLine(orderLine);

            if (commonDesignerId == null) {
                commonDesignerId = designerId;
            } else {
                if (!designerId.equals(commonDesignerId)) {
                    throw new BadRequestException("Alle producten in de bestelling moeten van dezelfde ontwerper zijn");
                }
            }
        }
        order.setAmount(totalPrice);
        order.setDesignerId(commonDesignerId);

        orderRepository.save(order);

        cart.getItems().clear();
        cartService.saveCart(cart);
    }

    public double calculateDiscountAmount(AbstractProduct product, int quantity) {
        Promotion promotion = product.getPromotion();
        if (promotion != null) {
            double discountPercentage = promotion.getPromotionPercentage() / 100.0;
            return product.getPrice() * quantity * discountPercentage;
        }
        return 0.0;
    }

    private AbstractProduct getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product niet gevonden"));
    }

    public OrderOutputDTO getOrderDetails(Long orderId) {
        User user = authenticationService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Bestelling niet gevonden"));

        if (!order.isOwnedBy(user.getUsername())) {
            throw new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te bekijken");
        }

        return toOrderOutputDTO(order);
    }

    public List<OrderOutputDTO> getUserOrders() {
        User user = authenticationService.getCurrentUser();
        List<Order> userOrders = orderRepository.findByUserUsername(user.getUsername());

        return userOrders.stream()
                .map(this::toOrderOutputDTO)
                .collect(Collectors.toList());
    }

    public void cancelOrder(Long orderId) {
        User user = authenticationService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Bestelling niet gevonden"));

        if (!order.isOwnedBy(user.getUsername())) {
            throw new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te annuleren");
        }

        order.updateOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    public ResponseEntity<String> confirmShipment(Long orderId) {
        try {
            User user = authenticationService.getCurrentUser();
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bestelling niet gevonden"));

            if (order.getOrderStatus() == OrderStatus.SHIPPED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Bestelling is al verzonden");
            }

            if (order.getPaymentStatus() != PaymentStatus.CONFIRMED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Betaling is niet bevestigd");
            }

            String designerUsername = user.getUsername();

            if (!order.containsProductsByDesigner(designerUsername)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("De bestelling bevat geen producten van de ontwerper die de verzending bevestigt");
            }

            order.setOrderStatus(OrderStatus.SHIPPED);

            Receipt receipt = generateReceipt(order);
            order.setReceipt(receipt);

            orderRepository.save(order);

            return ResponseEntity.ok("Verzending bevestigd voor bestelling: " + orderId);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Er is een fout opgetreden bij het bevestigen van de verzending.");
        }
    }


    public ReceiptOutputDTO getReceiptForOrder(Long orderId) {
        User user = authenticationService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Bestelling niet gevonden"));
        if (!order.isOwnedBy(user.getUsername())) {
            throw new AuthorizationServiceException("Je bent niet gemachtigd om deze bon te bekijken");
        }

        Receipt receipt = order.getReceipt();
        if (receipt == null) {
            throw new ResourceNotFoundException("Bon niet gevonden voor bestelling met ID: " + orderId);
        }

        return toReceiptOutputDTO(receipt);
    }

    private OrderOutputDTO toOrderOutputDTO(Order order) {
        OrderOutputDTO orderOutputDTO = new OrderOutputDTO();
        orderOutputDTO.setOrderId(order.getOrderId());
        orderOutputDTO.setUsername(order.getUser().getUsername());
        orderOutputDTO.setAmount(order.getAmount());
        orderOutputDTO.setOrderDateTime(order.getOrderDateTime());
        orderOutputDTO.setShippingAddress(order.getShippingAddress());
        orderOutputDTO.setOrderStatus(order.getOrderStatus());
        orderOutputDTO.setPaymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null);

        AbstractPaymentMethod paymentMethod = order.getPaymentMethod();
        if (paymentMethod != null) {
            orderOutputDTO.setPaymentMethodType(paymentMethod.getPaymentMethodType());
        }

        return orderOutputDTO;
    }

    private ReceiptOutputDTO toReceiptOutputDTO(Receipt receipt) {
        ReceiptOutputDTO outputDTO = new ReceiptOutputDTO();
        outputDTO.setReceiptId(receipt.getReceiptId());
        outputDTO.setDateIssued(receipt.getDateIssued());
        outputDTO.setTotalAmount(receipt.getTotalAmount());
        outputDTO.setShippingCost(receipt.getShippingCost());

        return outputDTO;
    }

    private Receipt generateReceipt(Order order) {
        Receipt receipt = new Receipt();
        receipt.setDateIssued(LocalDateTime.now());

        double totalAmount = order.calculateTotalAmount();
        receipt.setTotalAmount(totalAmount);

        double shippingCost = 0.0;
        receipt.setShippingCost(shippingCost);

        return receipt;
    }
}
