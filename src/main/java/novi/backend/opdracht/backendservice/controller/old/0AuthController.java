//package novi.backend.opdracht.backendservice.controller;
//
//import novi.backend.opdracht.backendservice.dto.AuthDto;
//import novi.backend.opdracht.backendservice.model.User;
//import novi.backend.opdracht.backendservice.repository.UserRepository;
//import novi.backend.opdracht.backendservice.security.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//public class AuthController {
//
//    private final JwtService jwtService;
//    private final AuthenticationManager authManager;
//    private final UserRepository userRepository;
//
//    @Autowired // This can be omitted if you're using constructor injection only
//    public AuthController(AuthenticationManager man, JwtService service, UserRepository userRepository) {
//        this.authManager = man;
//        this.jwtService = service;
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/auth")
//    public ResponseEntity<Object> signIn(@RequestBody AuthDto authDto) {
//        Optional<User> userOptional = userRepository.findByUsername(authDto.username);
//
//        // Check if user exists
//        if (userOptional.isEmpty()) {
//            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(authDto.username, authDto.password);
//
//        try {
//            Authentication auth = authManager.authenticate(authenticationToken);
//
//            UserDetails userDetails = (UserDetails) auth.getPrincipal();
//            String token = jwtService.generateToken(userDetails);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                    .body("Token generated");
//        } catch (AuthenticationException ex) {
//            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
//        }
//    }
//}
