package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "designers")
public class Designer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long designerId;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Promotion> promotions;

    public Designer() {
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public double calculateTotalSales(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getDesignerId().equals(this.designerId))
                .mapToDouble(Order::getAmount)
                .sum();
    }

    public double calculateTotalPromotions(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getDesignerId().equals(this.designerId))
                .mapToDouble(Order::calculateTotalDiscount)
                .sum();
    }

    private double calculatePromotions(Order order) {
        return order.getOrderLines().stream()
                .mapToDouble(orderLine -> {
                    AbstractProduct product = orderLine.getProduct();
                    if (product != null && product.getPromotion() != null) {
                        double promotionPercentage = product.getPromotion().getPromotionPercentage();
                        return (orderLine.getPrice() * orderLine.getQuantity() * promotionPercentage) / 100.0;
                    }
                    return 0.0;
                })
                .sum();
    }
}
