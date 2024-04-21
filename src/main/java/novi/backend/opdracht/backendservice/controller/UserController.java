package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.*;
import novi.backend.opdracht.backendservice.dto.output.UserResponse;
import novi.backend.opdracht.backendservice.model.Role;
import novi.backend.opdracht.backendservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.validation.FieldError;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> userResponses = userService.getUsers();
        return ResponseEntity.ok().body(userResponses);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("username") String username) {
        UserResponse userResponse = userService.getUser(username);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping(value = "")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequest request, BindingResult result) {
        if (result.hasFieldErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fe : result.getFieldErrors()) {
                stringBuilder.append(fe.getField()).append(": ");
                stringBuilder.append(fe.getDefaultMessage());
                stringBuilder.append("\n");
            }
            return ResponseEntity.badRequest().body(stringBuilder.toString());
        }
        String newUsername = userService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{username}/update-information")
    public ResponseEntity<Object> updateUserInformation(@PathVariable("username") String username, @Valid @RequestBody UpdateUserRequest updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                stringBuilder.append(fe.getField()).append(": ");
                stringBuilder.append(fe.getDefaultMessage());
                stringBuilder.append("\n");
            }
            return ResponseEntity.badRequest().body(stringBuilder.toString());
        }
        userService.updateUserInformation(username, updateRequest);
        UserResponse updatedUser = userService.getUser(username);
        return ResponseEntity.ok(updatedUser);
    }


    @PutMapping(value = "/{username}/password")
    public ResponseEntity<Object> updateUserPassword(@PathVariable("username") String username, @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                stringBuilder.append(fe.getField()).append(": ");
                stringBuilder.append(fe.getDefaultMessage());
                stringBuilder.append("\n");
            }
            return ResponseEntity.badRequest().body(stringBuilder.toString());
        }
        userService.updateUserPassword(username, passwordUpdateRequest.getCurrentPassword(), passwordUpdateRequest.getNewPassword());
        UserResponse updatedUser = userService.getUser(username);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping(value = "/{username}/delete-account")
    public ResponseEntity<Object> deleteUserAccount(@PathVariable("username") String username, @RequestBody DeleteAccountRequest deleteAccountRequest) {
        userService.deleteUserAccount(username, deleteAccountRequest.getConfirmUsername(), deleteAccountRequest.getConfirmPassword());

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        String authorityName = (String) fields.get("authority");
        Role role = Role.valueOf(authorityName);
        userService.addAuthority(username, role);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") Role authority) {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }
}
