package novi.backend.opdracht.backendservice.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String message) {
        super(message);
    }

    public UsernameExistsException() {
        super();
    }
}
