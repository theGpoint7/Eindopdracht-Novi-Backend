package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDTO;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_method_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private LocalDateTime paymentCreationDateTime;

    @Column
    private LocalDateTime paymentMethodExpirationDateTime;

    @Column
    private LocalDateTime paymentCompletionDateTime;

    // Getters and Setters

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

    public void confirmPayment() {
        this.paymentCompletionDateTime = LocalDateTime.now();
    }

    public String getPaymentMethodType() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    public boolean matchesPaymentDetails(PaymentConfirmationRequestDTO requestDTO) {
        return false;
    }
}
