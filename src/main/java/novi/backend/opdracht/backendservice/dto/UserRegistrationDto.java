package novi.backend.opdracht.backendservice.dto;

public class UserRegistrationDto {
    private UserDto user;
    private UserCredentialsDto credentials;

    // Constructors
    public UserRegistrationDto() {}

    // Getters and setters
    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserCredentialsDto getCredentials() {
        return credentials;
    }

    public void setCredentials(UserCredentialsDto credentials) {
        this.credentials = credentials;
    }
}
