//package novi.backend.opdracht.backendservice.controller;
//
//import jakarta.validation.Valid;
//import novi.backend.opdracht.backendservice.dto.DesignerRequestDto;
//import novi.backend.opdracht.backendservice.dto.UserDto;
//import novi.backend.opdracht.backendservice.dto.UserRegistrationDto;
//import novi.backend.opdracht.backendservice.service.UserService0;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//public class UserController0 {
//
//    private final UserService0 userService;
//
//    public UserController0(UserService0 userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/users")
//    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegistrationDto registrationDto, BindingResult result) {
//        if (result.hasFieldErrors()) {
//            StringBuilder sb = new StringBuilder();
//            for (FieldError fe : result.getFieldErrors()) {
//                sb.append(fe.getField() + ": ");
//                sb.append(fe.getDefaultMessage());
//                sb.append("/n");
//            }
//            return ResponseEntity.badRequest().body(sb.toString());
//        }
//        Long newUserId = userService.createUser(registrationDto);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "/users/" + newUserId);
//        return ResponseEntity.ok().headers(headers).body("User created successfully! ID: " + newUserId);
//    }
//
//    @PostMapping("/users/{userId}/designer-requests")
//    public ResponseEntity<?> submitDesignerRequest(@PathVariable Long userId, @RequestBody DesignerRequestDto requestDto) {
//        Long newRequestId = userService.submitDesignerRequest(userId, requestDto);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "/users/" + userId + "/designer-requests/" + newRequestId);
//        return ResponseEntity.ok().headers(headers).body("Designer request submitted successfully! Request ID: " + newRequestId);
//    }
//
//    @PutMapping("/users/{userId}")
//    public ResponseEntity<String> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto, BindingResult result) {
//        if (result.hasFieldErrors()) {
//            return ResponseEntity.badRequest().body("Validation errors: " + result.getAllErrors());
//        }
//        userService.updateUser(userId, userDto);
//        return ResponseEntity.ok("User updated successfully!");
//    }
//
//
//    @DeleteMapping("/users/{userId}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//        return ResponseEntity.ok("User deleted successfully!");
//    }
//
//    @PostMapping("/users/{userId}/roles/{roleName}")
//    public ResponseEntity<String> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
//        userService.addRoleToUser(userId, roleName);
//        return ResponseEntity.ok("Role added to user successfully!");
//    }
//
//    @DeleteMapping("/users/{userId}/roles/{roleName}")
//    public ResponseEntity<String> removeRoleFromUser(@PathVariable Long userId, @PathVariable String roleName) {
//        userService.removeRoleFromUser(userId, roleName);
//        return ResponseEntity.ok("Role removed from user successfully!");
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity<?> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<UserDto> getUserDto(@PathVariable Long userId) {
//        UserDto userDto = userService.getUser(userId);
//        return ResponseEntity.ok(userDto);
//    }
//
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
//        return ResponseEntity.ok(userService.findUserById(userId));
//    }
//}
