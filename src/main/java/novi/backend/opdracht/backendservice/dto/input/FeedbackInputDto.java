package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.*;

public class FeedbackInputDto {

    private Long productId;
    private Long designerId;

    @NotBlank(message = "Feedbackinhoud mag niet leeg zijn.")
    @Size(min = 5, max = 1000, message = "Feedbackinhoud moet tussen 5 en 1000 tekens bevatten.")
    private String content;

    private String username;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
