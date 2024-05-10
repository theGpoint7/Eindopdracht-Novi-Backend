package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DesignerInfoInputDto {
    @NotBlank(message = "Winkelnaam mag niet leeg zijn.")
    @Size(min = 2, max = 100, message = "Winkelnaam moet tussen de 2 en 100 karakters lang zijn.")
    private String storeName;

    @Size(max = 500, message = "Bio moet minder dan 500 karakters lang zijn.")
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
