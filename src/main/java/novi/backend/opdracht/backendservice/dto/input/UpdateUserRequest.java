package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UpdateUserRequest {

    @NotBlank(message = "E-mailadres mag niet leeg zijn.")
    @Email(message = "Ongeldig e-mailformaat, tenzij je van toeval houdt en graag Russische spam ontvangt.")
    private String email;

    @NotBlank(message = "Voornaam mag niet leeg zijn, anders kun je geen vrienden maken op feestjes.")
    @Size(min = 2, max = 128, message = "Voornaam moet tussen de 2 en 128 karakters lang zijn.")
    private String firstName;

    @NotBlank(message = "Achternaam mag niet leeg zijn, anders raakt de stamboom van je familie in de war.")
    @Size(min = 2, max = 128, message = "Achternaam moet tussen de 2 en 128 karakters lang zijn.")
    private String lastName;

    @Past(message = "Geboortedatum moet in het verleden liggen")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Adres mag niet leeg zijn, anders vindt de pizzabezorger je niet.")
    private String address;

    @NotBlank(message = "Telefoonnummer mag niet leeg zijn, anders kun je niet opscheppen over je nieuwe telefoon.")
    private String phoneNo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
