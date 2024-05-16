package novi.backend.opdracht.backendservice.exception;

public class ProductNameTooLongException extends RuntimeException{
    public ProductNameTooLongException(String message){
        super(message);
    }
}