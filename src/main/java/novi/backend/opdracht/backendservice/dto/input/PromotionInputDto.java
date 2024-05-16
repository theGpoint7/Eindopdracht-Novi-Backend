package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PromotionInputDto {

    @NotBlank(message = "Promotienaam mag niet leeg zijn.")
    @Size(max = 255, message = "Promotienaam mag niet langer zijn dan 255 tekens.")
    private String promotionName;

    @Size(max = 1000, message = "Promotiebeschrijving moet korter zijn dan 1000 tekens.")
    private String promotionDescription;

    @NotNull(message = "Promotiepercentage mag niet leeg zijn.")
    private double promotionPercentage;

    private LocalDateTime promotionStartDateTime;
    private LocalDateTime promotionEndDateTime;

    public PromotionInputDto() {
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
