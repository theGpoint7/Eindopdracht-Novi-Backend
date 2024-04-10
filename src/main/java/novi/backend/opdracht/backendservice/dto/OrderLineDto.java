package novi.backend.opdracht.backendservice.dto;

public class OrderLineDto {
    private ProductDto product;
    private int quantity;

    // getters and setters
    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
