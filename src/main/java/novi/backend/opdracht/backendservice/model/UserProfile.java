package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private String username;

    @MapsId
    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String address;
    private String phoneNo;

    // Getters and setters
}

