package novi.backend.opdracht.backendservice.exception;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException() {
        super();
    }
    public InvalidProductException(String message) {
        super(message);
    }
}