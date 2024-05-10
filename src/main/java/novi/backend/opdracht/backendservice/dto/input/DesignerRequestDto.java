package novi.backend.opdracht.backendservice.dto.input;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class DesignerRequestDto {
    @NotBlank(message = "KVK Number is required")
    private String kvkNumber;

    public String getKvkNumber() {
        return kvkNumber;
    }

    public void setKvkNumber(String kvkNumber) {
        this.kvkNumber = kvkNumber;
    }
}