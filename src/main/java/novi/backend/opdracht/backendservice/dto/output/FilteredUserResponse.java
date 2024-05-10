package novi.backend.opdracht.backendservice.dto.output;


public class FilteredUserResponse {
    private String username;
    private String lastName;
    private String email;


    public FilteredUserResponse() {
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}


