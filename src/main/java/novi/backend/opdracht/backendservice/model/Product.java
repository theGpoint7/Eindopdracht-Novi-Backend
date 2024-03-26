package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(name = "inventory_count", nullable = false)
    private Integer inventoryCount;

    @Column(name = "image_url")
    private String imageUrl;

    // Constructors, getters, and setters
}
