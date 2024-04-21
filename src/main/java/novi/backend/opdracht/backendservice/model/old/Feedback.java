//package novi.backend.opdracht.backendservice.model;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "feedback")
//public class Feedback {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    // Assuming you want to connect feedback directly to a designer
//    @ManyToOne
//    @JoinColumn(name = "designer_id")
//    private DesignerProfile designer;
//
//    private int rating; // 1-5 for example
//    private String comment;
//
//    // Getters and setters
//}
