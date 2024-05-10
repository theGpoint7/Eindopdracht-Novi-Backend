package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

@Entity
public class Footwear extends AbstractProduct {

    private Integer footwearSize;
    private String gender;

    public Footwear() {
    }

    public Footwear(String productName, String productType, double price, int inventoryCount, String imageUrl, String productDescription, String material, Integer footwearSize, String gender) {
        this.footwearSize = footwearSize;
        this.gender = gender;
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
