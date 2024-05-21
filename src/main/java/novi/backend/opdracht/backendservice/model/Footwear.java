package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;

@Entity
public class Footwear extends AbstractProduct {

    private Integer footwearSize;
    private String gender;

    public Footwear() {
        super();
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

    public void insertSpecificFields(ProductInputDto productInputDTO) {
        this.footwearSize = productInputDTO.getFootwearSize();
        this.gender = productInputDTO.getGender();
    }

    public void updateSpecificFields(ProductUpdateDto productUpdateDTO) {
        this.footwearSize = productUpdateDTO.getFootwearSize();
        this.gender = productUpdateDTO.getGender();
    }
}
