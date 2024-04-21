//package novi.backend.opdracht.backendservice.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "orders")
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User0 user;
//
//    @Column(name = "order_date")
//    private LocalDateTime orderDate;
//
//    private double subtotal;
//
//    private double shippingCosts;
//
//    private double total;
//
//    @ManyToOne
//    @JoinColumn(name = "payment_method_id")
//    private PaymentMethod paymentMethod;
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "payment_status")
//    private PaymentStatus paymentStatus;
//
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OrderLine> orderLines = new ArrayList<>();
//
//    @Column(name = "delivery_address")
//    private String deliveryAddress;
//
//    // Constructors
//
//    public Order() {
//        // Default constructor
//    }
//
//    public Order(User0 user, LocalDateTime orderDate, OrderStatus status) {
//        this.user = user;
//        this.orderDate = orderDate;
//        this.status = OrderStatus.PENDING;
//        this.paymentStatus = PaymentStatus.PENDING;
//    }
//
//    // Getters and Setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public User0 getUser() {
//        return user;
//    }
//
//    public void setUser(User0 user) {
//        this.user = user;
//    }
//
//    public LocalDateTime getOrderDate() {
//        return orderDate;
//    }
//
//    public void setOrderDate(LocalDateTime orderDate) {
//        this.orderDate = orderDate;
//    }
//
//    public double getSubtotal() {
//        return subtotal;
//    }
//
//    public void setSubtotal(double subtotal) {
//        this.subtotal = subtotal;
//    }
//
//    public double getShippingCosts() {
//        return shippingCosts;
//    }
//
//    public void setShippingCosts(double shippingCosts) {
//        this.shippingCosts = shippingCosts;
//    }
//
//    public double getTotal() {
//        return total;
//    }
//
//    public void setTotal(double total) {
//        this.total = total;
//    }
//
//    public PaymentMethod getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public void setPaymentMethod(PaymentMethod paymentMethod) {
//        this.paymentMethod = paymentMethod;
//    }
//
//    public OrderStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(OrderStatus status) {
//        this.status = status;
//    }
//
//    public PaymentStatus getPaymentStatus() {
//        return paymentStatus;
//    }
//
//    public void setPaymentStatus(PaymentStatus paymentStatus) {
//        this.paymentStatus = paymentStatus;
//    }
//
//    public List<OrderLine> getOrderLines() {
//        return orderLines;
//    }
//
//    public void setOrderLines(List<OrderLine> orderLines) {
//        this.orderLines = orderLines;
//    }
//
//    public String getDeliveryAddress() {
//        return deliveryAddress;
//    }
//
//    public void setDeliveryAddress(String deliveryAddress) {
//        this.deliveryAddress = deliveryAddress;
//    }
//
//    // Helper Methods
//
//    public void addOrderLine(OrderLine orderLine) {
//        orderLines.add(orderLine);
//        orderLine.setOrder(this);
//    }
//
//    public void removeOrderLineById(Long orderLineId) {
//        OrderLine orderLineToRemove = null;
//        for (OrderLine orderLine : orderLines) {
//            if (orderLine.getId().equals(orderLineId)) {
//                orderLineToRemove = orderLine;
//                break;
//            }
//        }
//        if (orderLineToRemove != null) {
//            orderLines.remove(orderLineToRemove);
//            orderLineToRemove.setOrder(null);
//        }
//    }
//}