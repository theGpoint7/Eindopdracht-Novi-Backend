package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDTO;

@Entity
public class Accessory extends AbstractProduct {

    @Enumerated(EnumType.STRING)
    private AccessoryType accessoryType;

    public Accessory() {
        super();
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(AccessoryType accessoryType) {
        this.accessoryType = accessoryType;
    }

    public void insertSpecificFields(ProductInputDTO productInputDTO) {
        this.accessoryType = AccessoryType.valueOf(productInputDTO.getAccessoryType());
    }

    public void updateSpecificFields(ProductUpdateDTO productUpdateDTO) {
        this.accessoryType = AccessoryType.valueOf(productUpdateDTO.getAccessoryType());
    }
}
