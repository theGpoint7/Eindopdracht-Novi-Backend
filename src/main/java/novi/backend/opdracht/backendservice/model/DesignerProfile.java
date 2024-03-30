package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "designer_profiles")
public class DesignerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Column(length = 1000)
    private String bio;

    @Column(length = 1000)
    private String portfolio;

    // Getters and setters
}
