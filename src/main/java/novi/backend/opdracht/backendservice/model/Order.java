package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderLine> orderLines = new HashSet<>();

    @Column(name = "designer_id")
    private Long designerId;

    @OneToOne
    @JoinColumn(name = "paymentMethodId")
    private AbstractPaymentMethod paymentMethod;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private Receipt receipt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Order() {
        this.orderLines = new HashSet<>();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getDesignerId() {
        return designerId;
    }

    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }

    public Set<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public AbstractPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(AbstractPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void updateOrderStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.CANCELED && this.orderStatus == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Bestelling is al verzonden en kan niet worden geannuleerd");
        }
        if (newStatus == OrderStatus.CANCELED && this.orderStatus == OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Bestelling is al voltooid en kan niet worden geannuleerd");
        }
        this.orderStatus = newStatus;
    }

    public boolean isOwnedBy(String username) {
        return this.user.getUsername().equals(username);
    }

    public double calculateTotalAmount() {
        return orderLines.stream()
                .mapToDouble(OrderLine::calculateTotalPrice)
                .sum();
    }

    public void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
    }

    public boolean containsProductsByDesigner(String designerUsername) {
        return orderLines.stream()
                .allMatch(orderLine -> orderLine.getProduct().getDesigner().getUser().getUsername().equals(designerUsername));
    }

    public double calculateTotalDiscount() {
        return orderLines.stream()
                .filter(orderLine -> orderLine.getProduct().getPromotion() != null)
                .mapToDouble(orderLine -> {
                    AbstractProduct product = orderLine.getProduct();
                    Promotion promotion = product.getPromotion();
                    return (orderLine.getPrice() * orderLine.getQuantity() * promotion.getPromotionPercentage()) / 100.0;
                })
                .sum();
    }
}
