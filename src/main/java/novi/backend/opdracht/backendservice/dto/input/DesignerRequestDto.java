package novi.backend.opdracht.backendservice.dto.input;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class DesignerRequestDto {
    @NotBlank(message = "KVK nummer is verplicht")
    @Size(min = 8, max = 16, message = "KVK nummer moet tussen de 8 en 16 tekens bevatten.")
    private String kvkNumber;

    public String getKvkNumber() {
        return kvkNumber;
    }

    public void setKvkNumber(String kvkNumber) {
        this.kvkNumber = kvkNumber;
    }
}