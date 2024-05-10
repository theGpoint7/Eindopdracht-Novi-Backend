package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotions", uniqueConstraints = @UniqueConstraint(columnNames = "promotionName"))
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    @Column(nullable = false)
    private String promotionName;

    @Column(columnDefinition = "TEXT")
    private String promotionDescription;

    @Column(nullable = false)
    private double promotionPercentage;

    @Column(nullable = false)
    private LocalDateTime promotionStartDateTime;

    @Column(nullable = false)
    private LocalDateTime promotionEndDateTime;

    @ManyToOne
    @JoinColumn(name = "designerId", nullable = false)
    private Designer designer;

    @OneToMany(mappedBy = "promotion")
    private Set<AbstractProduct> products;


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

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }
    public Set<AbstractProduct> getProducts() {
        return products;
    }
    public void setProducts(Set<AbstractProduct> products) {
        this.products = products;
    }
}
