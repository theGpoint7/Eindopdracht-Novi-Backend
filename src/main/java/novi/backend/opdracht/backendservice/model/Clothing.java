package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;

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

    public void insertSpecificFields(ProductInputDto productInputDTO) {
        this.clothingSize = productInputDTO.getClothingSize();
        this.color = productInputDTO.getColor();
        this.fit = productInputDTO.getFit();
    }

    public void updateSpecificFields(ProductUpdateDto productUpdateDTO) {
        this.clothingSize = productUpdateDTO.getClothingSize();
        this.color = productUpdateDTO.getColor();
        this.fit = productUpdateDTO.getFit();
    }
}
