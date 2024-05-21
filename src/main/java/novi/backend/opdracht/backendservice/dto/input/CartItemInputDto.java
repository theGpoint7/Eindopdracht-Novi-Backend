package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.*;

public class CartItemInputDto {
    @NotNull
    private Long productId;

    @NotNull
    private int quantity;

    public CartItemInputDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItemInputDto() {

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
