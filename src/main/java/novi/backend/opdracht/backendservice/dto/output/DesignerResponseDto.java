package novi.backend.opdracht.backendservice.dto.output;

import java.util.List;

public class DesignerResponseDto {

    private Long designerId;
    private String storeName;
    private String bio;
    private List<FeedbackOutputDTO> feedbacks;

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<FeedbackOutputDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackOutputDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
