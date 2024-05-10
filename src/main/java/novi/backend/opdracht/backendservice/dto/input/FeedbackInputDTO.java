package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class FeedbackInputDTO {

    private Long productId;
    private Long designerId;

    @NotBlank(message = "Feedback content must not be blank")
    @Size(min = 5, max = 1000, message = "Feedback content must be between 5 and 1000 characters")
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
