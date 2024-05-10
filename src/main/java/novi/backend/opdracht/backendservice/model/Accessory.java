package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

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
}
