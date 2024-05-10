package novi.backend.opdracht.backendservice.dto.input;

public class DesignerApprovalDto {
    private boolean approve;
    private String rejectionReason;

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}