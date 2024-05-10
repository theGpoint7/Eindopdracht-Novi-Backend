package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class PromotionApplyDto {

    @NotBlank(message = "Promotion name cannot be empty.")
    private String promotionName;

    private LocalDateTime promotionStartDateTime;

    private LocalDateTime promotionEndDateTime;

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
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
