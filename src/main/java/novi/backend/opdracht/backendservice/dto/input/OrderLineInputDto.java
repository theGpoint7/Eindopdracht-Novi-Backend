package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.*;

public class OrderLineInputDto {
    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 1, message = "Minimale hoeveelheid is 1.")
    private int quantity;

    public OrderLineInputDto() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}