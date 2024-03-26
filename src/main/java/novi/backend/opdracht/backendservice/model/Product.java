package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int inventoryCount;

    private String imageUrl;

    // Relationship with DesignerProfile if needed
    // Relationship with Feedback if needed

    // Getters and setters
}
