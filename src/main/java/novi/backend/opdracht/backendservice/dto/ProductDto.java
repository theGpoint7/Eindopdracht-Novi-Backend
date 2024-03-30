package novi.backend.opdracht.backendservice.dto;

import java.math.BigDecimal;

public class ProductDto {
    private String name;
    private String description;
    private BigDecimal price;
    private int inventoryCount;
    private String imageUrl;

    // Default constructor for JSON deserialization
    public ProductDto() {}

    // Constructor with all fields
    public ProductDto(String name, String description, BigDecimal price, int inventoryCount, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventoryCount = inventoryCount;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
