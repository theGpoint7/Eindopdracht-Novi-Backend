package novi.backend.opdracht.backendservice.dto;

public class DesignerRequestDto {
    private String kvk; // Justification for the request

    // Constructors, getters, and setters
    public DesignerRequestDto() {}

    public String getKvk() {
        return kvk;
    }

    public void setKvk(String kvk) {
        this.kvk = kvk;
    }
}
