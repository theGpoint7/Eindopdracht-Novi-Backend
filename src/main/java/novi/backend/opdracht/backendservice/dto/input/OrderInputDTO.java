package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.*;
import java.util.List;

public class OrderInputDTO {
    @NotNull
    private String username;

    @NotNull
    private List<OrderLineInputDTO> orderLines;

    @NotNull
    private String paymentMethodType;

    @NotNull
    private double amount;

    @NotNull
    private String shippingAddress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderLineInputDTO> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLineInputDTO> orderLines) {
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}