package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDTO;

@Entity
public class CreditCardPayment extends AbstractPaymentMethod {

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public boolean matchesPaymentDetails(PaymentConfirmationRequestDTO requestDTO) {
        return this.cardNumber.equals(requestDTO.getCardNumber()) &&
                this.expiryDate.equals(requestDTO.getExpiryDate()) &&
                this.cvv.equals(requestDTO.getCvv());
    }

    @Override
    public String getPaymentMethodType() {
        return "CREDIT_CARD";
    }
}
