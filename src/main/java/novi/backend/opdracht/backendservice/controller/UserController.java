package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.DesignerRequestDto;
import novi.backend.opdracht.backendservice.dto.UserCredentialsDto;
import novi.backend.opdracht.backendservice.dto.UserDto;
import novi.backend.opdracht.backendservice.dto.UserRegistrationDto;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRequestRepository;
import novi.backend.opdracht.backendservice.repository.RoleRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class UserController {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    private final DesignerRequestRepository designerRequestRepo;

    public UserController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder, DesignerRequestRepository designerRequestRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.designerRequestRepo = designerRequestRepo;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationDto registrationDto) {
        UserDto userDto = registrationDto.getUser();
        UserCredentialsDto credentialsDto = registrationDto.getCredentials();

        User newUser = new User();
        UserCredentials newCredentials = new UserCredentials();
        UserProfile newUserProfile = new UserProfile();

        // Handling UserCredentials
        newCredentials.setUsername(credentialsDto.getUsername());
        newCredentials.setPasswordHash(encoder.encode(credentialsDto.getPassword()));
        newUser.setUserCredentials(newCredentials);
        newCredentials.setUser(newUser);

        // Set UserProfile information from UserDto
        // (Similar to how you've done for UserCredentials)
        newUserProfile.setFirstName(userDto.getFirstName());
        newUserProfile.setLastName(userDto.getLastName());
        newUserProfile.setEmail(userDto.getEmail());
        newUserProfile.setAddress(userDto.getAddress());
        newUserProfile.setPhoneNo(userDto.getPhoneNo());
        newUser.setUserProfile(newUserProfile);
        newUserProfile.setUser(newUser);

        // Handling roles with the "ROLE_" prefix
        Set<Role> userRoles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            String fullRoleName = "ROLE_" + roleName; // Prepending "ROLE_" to match the database entries
            roleRepo.findByRolename(fullRoleName).ifPresent(userRoles::add);
        }

        newUser.setRoles(userRoles);

        userRepo.save(newUser);

        return ResponseEntity.ok("User created successfully!");
    }

    @PostMapping("/users/{userId}/designer-requests")
    public ResponseEntity<?> submitDesignerRequest(@PathVariable Long userId, @RequestBody DesignerRequestDto requestDto) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        User user = userOptional.get();

        DesignerRequest newRequest = new DesignerRequest();
        newRequest.setUser(user);
        newRequest.setStatus(RequestStatus.PENDING);
        newRequest.setDateOfRequest(LocalDate.now());
        newRequest.setKvk(requestDto.getKvk());

        designerRequestRepo.save(newRequest);

        return ResponseEntity.ok("Designer request submitted successfully");
    }
}
