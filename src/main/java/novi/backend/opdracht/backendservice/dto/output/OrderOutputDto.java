package novi.backend.opdracht.backendservice.dto.output;

import novi.backend.opdracht.backendservice.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderOutputDto {
    private Long orderId;
    private String username;
    private List<OrderLineOutputDto> orderLines;
    private String paymentMethodType;
    private double amount;
    private LocalDateTime orderDateTime;
    private String shippingAddress;
    private OrderStatus orderStatus;
    private String paymentStatus;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderLineOutputDto> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLineOutputDto> orderLines) {
        this.orderLines = orderLines;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}