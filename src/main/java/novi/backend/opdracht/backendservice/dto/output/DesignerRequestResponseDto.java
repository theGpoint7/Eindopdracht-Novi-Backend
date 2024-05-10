package novi.backend.opdracht.backendservice.dto.output;

import java.time.LocalDateTime;

public class DesignerRequestResponseDto {

    private Long requestId;
    private String username;
    private String kvkNumber;
    private String status;
    private LocalDateTime requestDateTime;
    private LocalDateTime approvalDateTime;
    private LocalDateTime rejectionDateTime;
    private String rejectionReason;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKvkNumber() {
        return kvkNumber;
    }

    public void setKvkNumber(String kvkNumber) {
        this.kvkNumber = kvkNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
