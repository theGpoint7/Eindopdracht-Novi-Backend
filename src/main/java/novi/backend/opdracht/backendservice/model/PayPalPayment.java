package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDTO;

@Entity
public class PayPalPayment extends AbstractPaymentMethod {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean matchesPaymentDetails(PaymentConfirmationRequestDTO requestDTO) {
        return this.email.equals(requestDTO.getEmail()) &&
                this.password.equals(requestDTO.getPassword());
    }
}
