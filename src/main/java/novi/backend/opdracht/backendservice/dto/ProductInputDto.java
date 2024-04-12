package novi.backend.opdracht.backendservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;


public class ProductInputDto {
    @NotBlank
    public String name;
    @NotBlank
    public String description;
    @Min(1)
    public double price;
    @Min(0)
    public int inventoryCount;
    @NotBlank
    public String imageUrl;
}