package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.input.DisableAccountRequest;
import novi.backend.opdracht.backendservice.dto.input.PasswordUpdateRequest;
import novi.backend.opdracht.backendservice.dto.input.UpdateUserRequest;
import novi.backend.opdracht.backendservice.dto.input.UserRequest;
import novi.backend.opdracht.backendservice.dto.output.FilteredUserResponse;
import novi.backend.opdracht.backendservice.dto.output.UserResponse;
import novi.backend.opdracht.backendservice.model.Role;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.service.UserService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;

    public UserController(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @PostMapping(value = "")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequest request, BindingResult result) {
        if (result.hasFieldErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        String newUsername = userService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<FilteredUserResponse>> getUsers() {
        List<FilteredUserResponse> fullUserResponses = userService.getAllUsers();
        return ResponseEntity.ok(fullUserResponses);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUsername().equals(username)) {
            UserResponse userResponse = userService.getUserResponse(username);
            return ResponseEntity.ok(userResponse);
        } else {
            FilteredUserResponse filteredUserResponse = userService.getFilteredUserResponse(username);
            return ResponseEntity.ok(filteredUserResponse);
        }
    }

    @PutMapping(value = "/{username}/update-information")
    public ResponseEntity<?> updateUserInformation(@PathVariable("username") String username, @Valid @RequestBody UpdateUserRequest updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(bindingResult);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        UserResponse updatedUser = userService.updateUserInformation(username, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(value = "/{username}/update-password")
    public ResponseEntity<?> updateUserPassword(@PathVariable("username") String username, @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(bindingResult);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        UserResponse updatedUser = userService.updateUserPassword(username, passwordUpdateRequest.getCurrentPassword(), passwordUpdateRequest.getNewPassword());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(value = "/{username}/delete-account")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable("username") String username, @RequestBody DisableAccountRequest deleteAccountRequest) {
        userService.deleteUserAccount(username, deleteAccountRequest.getConfirmUsername(), deleteAccountRequest.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Set<Role>> getUserAuthorities(@PathVariable("username") String username) {
        Set<Role> authorities = userService.getAuthorities(username);
        return ResponseEntity.ok(authorities);
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Void> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        String authorityName = (String) fields.get("authority");
        Role role = Role.valueOf(authorityName);
        userService.addAuthority(username, role);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{username}/disable-account")
    public ResponseEntity<String> disableUserAccount(@PathVariable("username") String username, @RequestBody DisableAccountRequest disableAccountRequest) {
        userService.disableUserAccount(username, disableAccountRequest.getConfirmUsername(), disableAccountRequest.getConfirmPassword());
        return ResponseEntity.ok("Gebruikersaccount voor " + username + " is gedeactiveerd.");
    }
}
