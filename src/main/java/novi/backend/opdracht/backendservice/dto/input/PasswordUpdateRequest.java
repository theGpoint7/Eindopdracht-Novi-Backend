package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordUpdateRequest {
    private String currentPassword;
    @NotBlank(message = "Wachtwoord mag niet leeg zijn.")
    @Size(min = 6, max = 20, message = "Wachtwoord moet tussen de 6 en 20 karakters lang zijn.")
    private String newPassword;

    public PasswordUpdateRequest() {}

    public PasswordUpdateRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
