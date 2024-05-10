package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "designer_requests")
public class DesignerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(nullable = false)
    private String kvkNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DesignerRequestStatus status;

    @Column
    private LocalDateTime requestDateTime;

    @Column
    private LocalDateTime approvalDateTime;

    @Column
    private LocalDateTime rejectionDateTime;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    public DesignerRequest() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKvkNumber() {
        return kvkNumber;
    }

    public void setKvkNumber(String kvkNumber) {
        this.kvkNumber = kvkNumber;
    }

    public DesignerRequestStatus getStatus() {
        return status;
    }

    public void setStatus(DesignerRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(LocalDateTime requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public LocalDateTime getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(LocalDateTime approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public LocalDateTime getRejectionDateTime() {
        return rejectionDateTime;
    }

    public void setRejectionDateTime(LocalDateTime rejectionDateTime) {
        this.rejectionDateTime = rejectionDateTime;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
