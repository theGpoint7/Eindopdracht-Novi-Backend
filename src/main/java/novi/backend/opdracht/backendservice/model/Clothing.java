package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;

@Entity
public class Clothing extends AbstractProduct {

    private String clothingSize;
    private String color;
    private String fit;

    public Clothing() {
        super();
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

    public void updateSpecificFields(ProductInputDTO productInputDTO) {
        this.clothingSize = productInputDTO.getClothingSize();
        this.color = productInputDTO.getColor();
        this.fit = productInputDTO.getFit();
    }
}
