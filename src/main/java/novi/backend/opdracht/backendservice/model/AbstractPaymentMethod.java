package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public abstract class AbstractPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String paymentMethodType;

    @Column(nullable = false)
    private LocalDateTime paymentCreationDateTime;

    @Column
    private LocalDateTime paymentMethodExpirationDateTime;

    @Column
    private LocalDateTime paymentCompletionDateTime;

    public AbstractPaymentMethod() {
    }

    public AbstractPaymentMethod(String paymentMethodType, LocalDateTime paymentCreationDateTime,
                                 LocalDateTime paymentMethodExpirationDateTime,
                                 LocalDateTime paymentCompletionDateTime) {
        this.paymentMethodType = paymentMethodType;
        this.paymentCreationDateTime = paymentCreationDateTime;
        this.paymentMethodExpirationDateTime = paymentMethodExpirationDateTime;
        this.paymentCompletionDateTime = paymentCompletionDateTime;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public LocalDateTime getPaymentCreationDateTime() {
        return paymentCreationDateTime;
    }

    public void setPaymentCreationDateTime(LocalDateTime paymentCreationDateTime) {
        this.paymentCreationDateTime = paymentCreationDateTime;
    }

    public LocalDateTime getPaymentMethodExpirationDateTime() {
        return paymentMethodExpirationDateTime;
    }

    public void setPaymentMethodExpirationDateTime(LocalDateTime paymentMethodExpirationDateTime) {
        this.paymentMethodExpirationDateTime = paymentMethodExpirationDateTime;
    }

    public LocalDateTime getPaymentCompletionDateTime() {
        return paymentCompletionDateTime;
    }

    public void setPaymentCompletionDateTime(LocalDateTime paymentCompletionDateTime) {
        this.paymentCompletionDateTime = paymentCompletionDateTime;
    }
}
