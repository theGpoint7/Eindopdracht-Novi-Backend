package novi.backend.opdracht.backendservice.dto.input;

public class DisableAccountRequest {

    private String confirmUsername;
    private String confirmPassword;

    public String getConfirmUsername() {
        return confirmUsername;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
