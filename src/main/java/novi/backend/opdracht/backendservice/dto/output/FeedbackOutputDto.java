package novi.backend.opdracht.backendservice.dto.output;

import java.time.LocalDateTime;

public class FeedbackOutputDto {

    private Long feedbackId;
    private String username;
    private Long productId;
    private Long designerId;
    private String content;
    private LocalDateTime feedbackDateTime;

    public FeedbackOutputDto() {
    }

    public FeedbackOutputDto(Long feedbackId, String username, Long productId, Long designerId, String content, LocalDateTime feedbackDateTime) {
        this.feedbackId = feedbackId;
        this.username = username;
        this.productId = productId;
        this.designerId = designerId;
        this.content = content;
        this.feedbackDateTime = feedbackDateTime;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public LocalDateTime getFeedbackDateTime() {
        return feedbackDateTime;
    }

    public void setFeedbackDateTime(LocalDateTime feedbackDateTime) {
        this.feedbackDateTime = feedbackDateTime;
    }
}
