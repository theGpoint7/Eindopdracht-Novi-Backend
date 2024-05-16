package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private AbstractProduct product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column
    private double discountAmount;

    public OrderLine() {
    }

    public OrderLine(Order order, AbstractProduct product, int quantity, double price, double discountAmount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discountAmount = discountAmount;
    }

    public Long getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(Long orderLineId) {
        this.orderLineId = orderLineId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public AbstractProduct getProduct() {
        return product;
    }

    public void setProduct(AbstractProduct product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double calculateTotalPrice() {
        double totalPrice = quantity * product.getPrice();
        if (product.getPromotion() != null) {
            double discountPercentage = product.getPromotion().getPromotionPercentage() / 100.0;
            totalPrice = totalPrice * (1 - discountPercentage);
        }

        return totalPrice;
    }
}
