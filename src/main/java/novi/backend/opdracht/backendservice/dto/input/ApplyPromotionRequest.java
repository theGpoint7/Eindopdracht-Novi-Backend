package novi.backend.opdracht.backendservice.dto.input;

public class ApplyPromotionRequest {
    private Long promotionId;
    private Long productId;

    public Long getPromotionId() {
        return promotionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
