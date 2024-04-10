package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRequestRepository;
import novi.backend.opdracht.backendservice.repository.RoleRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AdminController {

    private final DesignerRequestRepository designerRequestRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Autowired
    public AdminController(DesignerRequestRepository designerRequestRepo, UserRepository userRepo, RoleRepository roleRepo) {
        this.designerRequestRepo = designerRequestRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @GetMapping("/admin/designer-requests")
    public ResponseEntity<?> getAllDesignerRequests() {
        Iterable<DesignerRequest> designerRequestsIterable = designerRequestRepo.findAll();
        List<String> formattedRequests = StreamSupport.stream(designerRequestsIterable.spliterator(), false)
                .map(request -> {
                    UserCredentials credentials = request.getUser().getUserCredentials();
                    String username = credentials.getUsername();
                    String kvk = request.getKvk();
                    RequestStatus status = request.getStatus();
                    Long requestId = request.getId();
                    return "Request id: " + requestId + " Requested by: " + username + ", KVK: " + kvk + ", Status: " + status.toString();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(formattedRequests);
    }



    @PutMapping("/admin/designer-requests/{requestId}")
    public ResponseEntity<?> reviewDesignerRequest(@PathVariable Long requestId, @RequestParam("status") String status) {
        DesignerRequest request = designerRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Designer request not found"));

        RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
        request.setStatus(newStatus);
        designerRequestRepo.save(request);

        if (newStatus == RequestStatus.APPROVED) {
            User user = request.getUser();

            // Check if the user already has the ROLE_DESIGNER
            boolean alreadyADesigner = user.getRoles().stream()
                    .anyMatch(role -> "ROLE_DESIGNER".equals(role.getRolename()));

            if (!alreadyADesigner) {
                Role designerRole = roleRepo.findByRolename("ROLE_DESIGNER")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role DESIGNER not found"));
                user.getRoles().add(designerRole);
                userRepo.save(user);
                return ResponseEntity.ok("Designer request approved and DESIGNER role added to the user.");
            } else {
                // Optionally, you could also return a different response if the user already had the role
                return ResponseEntity.ok("Designer request approved, but user was already a DESIGNER.");
            }
        }

        return ResponseEntity.ok("Request reviewed successfully");
    }

    @GetMapping("/admin/hello")
    public String sayHelloToAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "stranger"; // Default to stranger if not authenticated
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername(); // Get the username of the authenticated admin
        }

        return "Hello, admin " + username + "!";
    }


    @GetMapping("/admin/designer-requests/{requestId}")
    public ResponseEntity<?> viewDesignerRequest(@PathVariable Long requestId) {
        return designerRequestRepo.findById(requestId)
                .map(request -> {
                    // Assuming User entity has a method to get UserCredentials
                    UserCredentials credentials = request.getUser().getUserCredentials();
                    String username = credentials.getUsername(); // Now fetching the username from UserCredentials
                    String kvk = request.getKvk();
                    return ResponseEntity.ok("Request by: " + username + ", KVK: " + kvk + ", Status: " + request.getStatus().toString());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
