package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.*;

public class CartItemInputDTO {
    @NotNull
    private Long productId;

    @NotNull
    private int quantity;

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
