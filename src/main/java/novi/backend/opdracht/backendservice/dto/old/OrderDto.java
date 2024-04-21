//package novi.backend.opdracht.backendservice.dto;
//
//import novi.backend.opdracht.backendservice.model.PaymentStatus;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class OrderDto {
//    private Long id;
//    private LocalDateTime orderDate;
//    private String user;
//    private String deliveryAddress; // Changed from customDeliveryAddress
//    private List<OrderLineDto> orderLines;
//    private double subtotal;
//    private double shippingCosts;
//    private double total;
//    private String paymentMethod;
//    private PaymentStatus paymentStatus;
//
//    // Getters and setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
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
//    public String getUser() {
//        return user;
//    }
//
//    public void setUser(String user) {
//        this.user = user;
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
//    public List<OrderLineDto> getOrderLines() {
//        return orderLines;
//    }
//
//    public void setOrderLines(List<OrderLineDto> orderLines) {
//        this.orderLines = orderLines;
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
//    public String getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public void setPaymentMethod(String paymentMethod) {
//        this.paymentMethod = paymentMethod;
//    }
//
//    public PaymentStatus getPaymentStatus() {
//        return paymentStatus;
//    }
//
//    public void setPaymentStatus(PaymentStatus paymentStatus) {
//        this.paymentStatus = paymentStatus;
//    }
//}
