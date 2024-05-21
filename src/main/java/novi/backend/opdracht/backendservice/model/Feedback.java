package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import novi.backend.opdracht.backendservice.dto.input.FeedbackInputDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private AbstractProduct product;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private Designer designer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime feedbackDateTime;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AbstractProduct getProduct() {
        return product;
    }

    public void setProduct(AbstractProduct product) {
        this.product = product;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
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

    public void validateFeedbackInput(FeedbackInputDto feedbackInputDTO, ProductRepository productRepository, DesignerRepository designerRepository) {
        if (feedbackInputDTO.getProductId() == null && feedbackInputDTO.getDesignerId() == null) {
            throw new BadRequestException("Er moet een productId of een designerId worden opgegeven");
        }

        if (feedbackInputDTO.getProductId() != null) {
            if (!productRepository.existsById(feedbackInputDTO.getProductId())) {
                throw new BadRequestException("Product met ID " + feedbackInputDTO.getProductId() + " niet gevonden");
            }
        }

        if (feedbackInputDTO.getDesignerId() != null) {
            if (!designerRepository.existsById(feedbackInputDTO.getDesignerId())) {
                throw new BadRequestException("Ontwerper met ID " + feedbackInputDTO.getDesignerId() + " niet gevonden");
            }
        }
    }
}
