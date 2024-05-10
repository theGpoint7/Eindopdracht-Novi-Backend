package novi.backend.opdracht.backendservice.dto.output;

import java.time.LocalDateTime;

public class PromotionResponseDto {

    private Long promotionId;
    private String promotionName;
    private String promotionDescription;
    private double promotionPercentage;
    private LocalDateTime promotionStartDateTime;
    private LocalDateTime promotionEndDateTime;

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public double getPromotionPercentage() {
        return promotionPercentage;
    }

    public void setPromotionPercentage(double promotionPercentage) {
        this.promotionPercentage = promotionPercentage;
    }

    public LocalDateTime getPromotionStartDateTime() {
        return promotionStartDateTime;
    }

    public void setPromotionStartDateTime(LocalDateTime promotionStartDateTime) {
        this.promotionStartDateTime = promotionStartDateTime;
    }

    public LocalDateTime getPromotionEndDateTime() {
        return promotionEndDateTime;
    }

    public void setPromotionEndDateTime(LocalDateTime promotionEndDateTime) {
        this.promotionEndDateTime = promotionEndDateTime;
    }
}