//package novi.backend.opdracht.backendservice.dto;
//
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Min;
//
//public class ProductDto {
//    public Long id;
//    @NotBlank
//    public String name;
//    public String description;
//    //ipv bigdecimal nu double
//    @Min(1)
//    public double price;
//    public int inventoryCount;
//    public String imageUrl;
//
//    // Default constructor for JSON deserialization
//    public ProductDto() {}
//
//    // Constructor with all fields
//    public ProductDto(Long id, String name, String description, double price, int inventoryCount, String imageUrl) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.inventoryCount = inventoryCount;
//        this.imageUrl = imageUrl;
//    }
//
//    // Getters
//    public Long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public int getInventoryCount() {
//        return inventoryCount;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    // Setters
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public void setInventoryCount(int inventoryCount) {
//        this.inventoryCount = inventoryCount;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//}
