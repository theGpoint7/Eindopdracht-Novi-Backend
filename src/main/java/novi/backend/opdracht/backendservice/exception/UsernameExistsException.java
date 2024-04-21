package novi.backend.opdracht.backendservice.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String username) {
        super("Username already exists: " + username);
    }
}
