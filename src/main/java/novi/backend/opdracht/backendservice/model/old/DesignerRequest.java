//package novi.backend.opdracht.backendservice.model;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "designer_requests")
//public class DesignerRequest {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false, unique = true)
//    private User0 user;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private RequestStatus status;
//
//    @Column(nullable = false)
//    private LocalDate dateOfRequest;
//
//    @Column(nullable = false)
//    private String kvk;
//
//    public User0 getUser() {
//        return user;
//    }
//
//    public void setUser(User0 user) {
//        this.user = user;
//    }
//
//    public RequestStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(RequestStatus status) {
//        this.status = status;
//    }
//
//    public LocalDate getDateOfRequest() {
//        return dateOfRequest;
//    }
//
//    public void setDateOfRequest(LocalDate dateOfRequest) {
//        this.dateOfRequest = dateOfRequest;
//    }
//
//    public String getKvk() {
//        return kvk;
//    }
//
//    public void setKvk(String kvk) {
//        this.kvk = kvk;
//    }
//
//    public Long getId() {
//        return id;
//    }
//}
