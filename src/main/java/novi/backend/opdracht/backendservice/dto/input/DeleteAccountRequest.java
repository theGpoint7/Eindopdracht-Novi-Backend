package novi.backend.opdracht.backendservice.dto.input;

public class DeleteAccountRequest {

    private String confirmUsername;
    private String confirmPassword;

    public DeleteAccountRequest() {
    }

    public DeleteAccountRequest(String confirmUsername, String confirmPassword) {
        this.confirmUsername = confirmUsername;
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmUsername() {
        return confirmUsername;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
