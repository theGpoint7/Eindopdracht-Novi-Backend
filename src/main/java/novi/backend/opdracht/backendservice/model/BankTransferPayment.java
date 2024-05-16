package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDTO;

@Entity
@DiscriminatorValue("BANK_TRANSFER")
public class BankTransferPayment extends AbstractPaymentMethod {

    private String accountNumber;
    private String bankCode;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public boolean matchesPaymentDetails(PaymentConfirmationRequestDTO requestDTO) {
        return this.accountNumber.equals(requestDTO.getAccountNumber()) &&
                this.bankCode.equals(requestDTO.getBankCode());
    }
}
