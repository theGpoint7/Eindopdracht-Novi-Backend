//package novi.backend.opdracht.backendservice.dto.inputdtos;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import novi.backend.opdracht.backendservice.dto.UserDto;
//
//public class UserRegistrationInputDto {
//    private UserDto user;
//
//    @NotBlank
//    private String username;
//
//    @NotBlank
//    @Email
//    private String email;
//
//    @NotBlank
//    private String password;
//
//    // Constructors, Getters, and Setters
//    public UserRegistrationInputDto() {}
//
//    public UserDto getUser() {
//        return user;
//    }
//
//    public void setUser(UserDto user) {
//        this.user = user;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//}
