package novi.backend.opdracht.backendservice.exception;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException() {
        super();
    }
    public OrderCreationException(String message) {
        super(message);
    }
}