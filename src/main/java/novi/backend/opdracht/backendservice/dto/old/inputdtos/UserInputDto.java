//package novi.backend.opdracht.backendservice.dto.inputdtos;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//
//public class UserInputDto {
//    private String[] roles;
//
//    @NotBlank
//    @Size(min = 2, max = 128, message = "geen spaties, grootte tussen 2 en 128 karakters")
//    private String firstName;
//
//    @NotBlank
//    @Size(min = 2, max = 128, message = "geen spaties, grootte tussen 2 en 128 karakters")
//    private String lastName;
//
//    @NotBlank
//    @Email
//    private String email;
//
//    @NotBlank
//    private String address;
//
//    @NotBlank
//    private String phoneNo;
//
//    // Constructors, Getters, and Setters
//    public UserInputDto() {}
//
//    public String[] getRoles() {
//        return roles;
//    }
//
//    public void setRoles(String[] roles) {
//        this.roles = roles;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
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
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getPhoneNo() {
//        return phoneNo;
//    }
//
//    public void setPhoneNo(String phoneNo) {
//        this.phoneNo = phoneNo;
//    }
//}
