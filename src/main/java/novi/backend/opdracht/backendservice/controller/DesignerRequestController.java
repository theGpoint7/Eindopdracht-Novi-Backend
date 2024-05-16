package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.input.DesignerApprovalDto;
import novi.backend.opdracht.backendservice.dto.input.DesignerInfoInputDto;
import novi.backend.opdracht.backendservice.dto.input.DesignerRequestDto;
import novi.backend.opdracht.backendservice.dto.output.DesignerRequestResponseDto;
import novi.backend.opdracht.backendservice.service.DesignerRequestService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/designerrequest")
public class DesignerRequestController {

    private final DesignerRequestService designerService;
    private final ValidationService validationService;

    public DesignerRequestController(DesignerRequestService designerService, ValidationService validationService) {
        this.designerService = designerService;
        this.validationService = validationService;
    }

    @PostMapping(value = "/request")
    public ResponseEntity<Object> submitDesignerRequest(@Valid @RequestBody DesignerRequestDto designerRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Long requestId = designerService.submitDesignerRequest(designerRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{requestId}")
                .buildAndExpand(requestId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/request/{username}")
    public ResponseEntity<List<DesignerRequestResponseDto>> getDesignerRequestsByUsername(@PathVariable String username) {
        List<DesignerRequestResponseDto> responses = designerService.getDesignerRequestsByUsername(username);
        if (responses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDesignerInfo(@RequestBody @Valid DesignerInfoInputDto designerInfo, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            designerService.updateDesignerInfo(username, designerInfo);
            return ResponseEntity.ok("Designerinformatie succesvol bijgewerkt.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Kon designerinformatie niet bijwerken: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/requests/all")
    public ResponseEntity<List<DesignerRequestResponseDto>> getAllDesignerRequests() {
        List<DesignerRequestResponseDto> responses = designerService.getAllDesignerRequests();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/requests/{requestId}/approve")
    public ResponseEntity<?> approveDesignerRequest(
            @PathVariable Long requestId,
            @RequestBody DesignerApprovalDto approvalDto) {

        if (!approvalDto.isApprove() && (approvalDto.getRejectionReason() == null || approvalDto.getRejectionReason().isEmpty())) {
            return ResponseEntity.badRequest().body("Afkeuringsreden moet worden opgegeven bij afwijzing van een verzoek.");
        }

        try {
            designerService.processDesignerRequest(requestId, approvalDto.isApprove(), approvalDto.getRejectionReason());
            return ResponseEntity.ok("Verzoek " + (approvalDto.isApprove() ? "goedgekeurd" : "afgekeurd") + ".");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Fout bij het verwerken van het verzoek: " + ex.getMessage());
        }
    }
}
