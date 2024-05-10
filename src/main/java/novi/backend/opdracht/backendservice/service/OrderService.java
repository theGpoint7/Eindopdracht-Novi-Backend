package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.CartItemInputDTO;
import novi.backend.opdracht.backendservice.dto.input.OrderRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.OrderOutputDTO;

import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDTO;
import novi.backend.opdracht.backendservice.exception.AuthenticationException;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    public ResponseEntity<String> placeOrder(OrderRequestDTO orderRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartService.getUserCart(username);
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Kan geen bestelling plaatsen met een lege winkelwagen");
        }

        List<CartItemInputDTO> cartItems;
        if (orderRequest.getCartItems() == null) {
            cartItems = cart.getItems().stream()
                    .map(cartItem -> {
                        CartItemInputDTO inputDTO = new CartItemInputDTO();
                        inputDTO.setProductId(cartItem.getProduct().getProductId());
                        inputDTO.setQuantity(cartItem.getQuantity());
                        return inputDTO;
                    })
                    .collect(Collectors.toList());
        } else {
            cartItems = orderRequest.getCartItems();
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(cart.getUser().getAddress());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        double totalPrice = 0.0;
        Set<OrderLine> orderLines = new HashSet<>();
        Long commonDesignerId = null;
        for (CartItemInputDTO cartItem : cartItems) {
            AbstractProduct product = getProductById(cartItem.getProductId());
            if (product != null) {
                if (product.getInventoryCount() < cartItem.getQuantity()) {
                    return ResponseEntity.badRequest().body("Onvoldoende voorraad voor product: " + product.getProductName());
                }
                product.setInventoryCount(product.getInventoryCount() - cartItem.getQuantity());
                productRepository.save(product);

                double discountAmount = calculateDiscountAmount(product, cartItem.getQuantity());
                double productPrice = (product.getPrice() - discountAmount) * cartItem.getQuantity();
                totalPrice += productPrice;

                Long designerId = product.getDesigner().getDesignerId();

                OrderLine orderLine = new OrderLine(order, product, cartItem.getQuantity(), productPrice, discountAmount);
                orderLines.add(orderLine);

                if (commonDesignerId == null) {
                    commonDesignerId = designerId;
                } else {
                    if (!designerId.equals(commonDesignerId)) {
                        return ResponseEntity.badRequest().body("Alle producten in de bestelling moeten van dezelfde ontwerper zijn");
                    }
                }
            }
        }
        order.setOrderLines(orderLines);
        order.setAmount(totalPrice);
        order.setDesignerId(commonDesignerId);

        orderRepository.save(order);

        cart.getItems().clear();
        cartService.saveCart(cart);

        return ResponseEntity.ok("Bestelling succesvol geplaatst");
    }

    private double calculateDiscountAmount(AbstractProduct product, int quantity) {
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new BadRequestException("Bestelling niet gevonden");
        }
        Order order = optionalOrder.get();

        if (!order.getUser().getUsername().equals(username)) {
            throw new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te bekijken");
        }

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

    public List<OrderOutputDTO> getUserOrders() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> userOrders = orderRepository.findByUserUsername(username);

        return userOrders.stream()
                .map(order -> {
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
                })
                .collect(Collectors.toList());
    }


    public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new BadRequestException("Bestelling niet gevonden");
        }
        Order order = optionalOrder.get();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!order.getUser().getUsername().equals(username)) {
            throw new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te annuleren");
        }
        if (order.getOrderStatus() != OrderStatus.SHIPPED) {
            throw new BadRequestException("Bestelling is al verzonden en kan niet worden geannuleerd");
        }
        if (order.getOrderStatus() != OrderStatus.CONFIRMED) {
            throw new BadRequestException("Bestelling is al voltooid en kan niet worden geannuleerd");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    public ResponseEntity<String> confirmShipment(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Bestelling niet gevonden"));

            if (order.getOrderStatus() == OrderStatus.SHIPPED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Bestelling is al verzonden");
            }

            if (order.getPaymentStatus() != PaymentStatus.CONFIRMED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Betaling is niet bevestigd");
            }

            String designerUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!orderContainsProductsByDesigner(order, designerUsername)) {
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

    private boolean orderContainsProductsByDesigner(Order order, String designerUsername) {
        Set<OrderLine> orderLines = order.getOrderLines();
        for (OrderLine orderLine : orderLines) {
            AbstractProduct product = orderLine.getProduct();
            if (!product.getDesigner().getUser().getUsername().equals(designerUsername)) {
                return false;
            }
        }
        return true;
    }

    public Receipt getReceipt(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Bestelling niet gevonden"));
        return generateReceipt(order);
    }

    public ReceiptOutputDTO getReceiptForOrder(Long orderId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getUsername().equals(currentUsername)) {
            throw new AuthorizationServiceException("You are not authorized to view this receipt");
        }

        Receipt receipt = order.getReceipt();
        if (receipt == null) {
            throw new ResourceNotFoundException("Receipt not found for order with ID: " + orderId);
        }

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

        double totalAmount = calculateTotalAmount(order);
        receipt.setTotalAmount(totalAmount);

        double shippingCost = 0.0;
        receipt.setShippingCost(shippingCost);

        return receipt;
    }

    private double calculateTotalAmount(Order order) {
        return order.getOrderLines().stream()
                .mapToDouble(orderLine -> orderLine.getQuantity() * orderLine.getPrice())
                .sum();
    }

    public double getTotalSalesByDesignerId(Long designerId) {
        List<Order> designerOrders = orderRepository.findAllByDesignerId(designerId);

        return designerOrders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.SHIPPED)
                .mapToDouble(Order::getAmount)
                .sum();
    }
}
