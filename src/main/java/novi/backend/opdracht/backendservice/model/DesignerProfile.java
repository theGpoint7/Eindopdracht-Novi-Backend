package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;


@Entity
@Table(name = "designer_profiles")
public class DesignerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Column(length = 1000) // Assuming a bio will be a longer text
    private String bio;

    // Portfolio can be a collection of Products, but for simplicity let's leave it as a text field
    @Column(length = 1000)
    private String portfolio;

    // Getters and setters
}
