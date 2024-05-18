package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import novi.backend.opdracht.backendservice.dto.input.UpdateUserRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String address;

    @Column
    private String phoneNo;

    @Column(nullable = false)
    private boolean enabledAccount = false;

    @Column
    private LocalDate userCreatedOn;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Set<DesignerRequest> designerRequests = new HashSet<>();

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY)
    private Designer designer;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public boolean isEnabledAccount() {
        return enabledAccount;
    }

    public void setEnabledAccount(boolean enabledAccount) {
        this.enabledAccount = enabledAccount;
    }

    public LocalDate getUserCreatedOn() {
        return userCreatedOn;
    }

    public void setUserCreatedOn(LocalDate userCreatedOn) {
        this.userCreatedOn = userCreatedOn;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void addFeedback(Feedback feedback) {
        this.feedbacks.add(feedback);
    }

    public void removeFeedback(Feedback feedback) {
        this.feedbacks.remove(feedback);
    }

    public Set<DesignerRequest> getDesignerRequests() {
        return designerRequests;
    }

    public void addDesignerRequest(DesignerRequest designerRequest) {
        this.designerRequests.add(designerRequest);
    }

    public void removeDesignerRequest(DesignerRequest designerRequest) {
        this.designerRequests.remove(designerRequest);
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public void enableAccount() {
        this.enabledAccount = true;
    }

    public void disableAccount() {
        this.enabledAccount = false;
    }

    public void updateInformation(UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.getEmail() != null) {
            this.email = updateUserRequest.getEmail();
        }
        if (updateUserRequest.getFirstName() != null) {
            this.firstName = updateUserRequest.getFirstName();
        }
        if (updateUserRequest.getLastName() != null) {
            this.lastName = updateUserRequest.getLastName();
        }
        if (updateUserRequest.getDateOfBirth() != null) {
            this.dateOfBirth = updateUserRequest.getDateOfBirth();
        }
        if (updateUserRequest.getAddress() != null) {
            this.address = updateUserRequest.getAddress();
        }
        if (updateUserRequest.getPhoneNo() != null) {
            this.phoneNo = updateUserRequest.getPhoneNo();
        }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
