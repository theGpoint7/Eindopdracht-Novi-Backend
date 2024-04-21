//package novi.backend.opdracht.backendservice.model;
//
//import jakarta.persistence.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "promotions")
//public class Promotion {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private LocalDate startDate;
//
//    @Column(nullable = false)
//    private LocalDate endDate;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "designer_id")
//    private DesignerProfile designer;
//
//    @Column(nullable = false)
//    private BigDecimal salePercentage;
//
//    // Getters and setters
//}
