package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;

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

    public void updateSpecificFields(ProductInputDTO productInputDTO) {
        this.accessoryType = AccessoryType.valueOf(productInputDTO.getAccessoryType());
    }
}
