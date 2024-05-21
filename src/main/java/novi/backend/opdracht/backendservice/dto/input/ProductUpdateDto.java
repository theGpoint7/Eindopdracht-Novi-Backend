package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductUpdateDto {
    @NotBlank(message = "Productnaam mag niet leeg zijn.")
    @Size(max = 255, message = "Productnaam mag maximaal 255 karakters bevatten.")
    private String productName;

    @NotNull(message = "Prijs mag niet leeg zijn.")
    @Min(value = 1, message = "Prijs moet minimaal 1 zijn.")
    private double price;

    @NotNull(message = "Voorraadtelling mag niet leeg zijn.")
    @Min(value = 0, message = "Voorraadtelling moet positief zijn of nul.")
    private int inventoryCount;

    @Size(max = 2083, message = "De URL van de afbeelding moet korter zijn dan 2083 tekens.")
    private String imageUrl;

    @Size(max = 1000, message = "Productbeschrijving moet korter zijn dan 1000 tekens.")
    private String productDescription;

    @Size(max = 255, message = "Materiaalbeschrijving moet korter zijn dan 255 tekens.")
    private String material;

    private String clothingSize;
    private String color;
    private String fit;

    private String accessoryType;

    private Integer footwearSize;
    private String gender;
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getClothingSize() {
        return clothingSize;
    }

    public void setClothingSize(String clothingSize) {
        this.clothingSize = clothingSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    public Integer getFootwearSize() {
        return footwearSize;
    }

    public void setFootwearSize(Integer footwearSize) {
        this.footwearSize = footwearSize;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}