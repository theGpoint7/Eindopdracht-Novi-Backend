package novi.backend.opdracht.backendservice.dto.input;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class UserRequest {
    @NotBlank(message = "Gebruikersnaam mag niet leeg zijn.")
    @Pattern(regexp = "^[a-z0-9._-]+$", message = "Gebruikersnaam mag alleen kleine letters, cijfers, punten, streepjes of underscores bevatten")
    private String username;

    @NotBlank(message = "Wachtwoord mag niet leeg zijn.")
    @Size(min = 6, max = 20, message = "Wachtwoord moet tussen de 6 en 20 karakters lang zijn.")
    private String password;

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

    private LocalDate userCreatedOn;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDate getUserCreatedOn() {
        return userCreatedOn;
    }

    public void setUserCreatedOn(LocalDate userCreatedOn) {
        this.userCreatedOn = userCreatedOn;
    }


}
