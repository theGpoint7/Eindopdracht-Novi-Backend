package novi.backend.opdracht.backendservice.dto.output;

public class DesignerInfoOutputDto {
    private String storeName;
    private String bio;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}