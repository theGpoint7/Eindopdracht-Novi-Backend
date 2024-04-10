package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_credentials", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
public class UserCredentials {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private User user;

    @Column(unique = true)
    private String username;
    private String passwordHash;

    // Getters and setters for the userId field
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getters and setters for the user field
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getters and setters for the username field
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters and setters for the passwordHash field
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
