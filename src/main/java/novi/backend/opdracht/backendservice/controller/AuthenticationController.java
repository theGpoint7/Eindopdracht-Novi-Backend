package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.input.AuthenticationRequest;
import novi.backend.opdracht.backendservice.dto.output.AuthenticationResponse;
import novi.backend.opdracht.backendservice.service.AuthenticationService;
import novi.backend.opdracht.backendservice.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthenticationService authenticationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Principal principal) {
        if (principal instanceof Authentication authentication) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("username", authentication.getName());
            response.put("authorities", authentication.getAuthorities());
            response.put("authenticated", authentication.isAuthenticated());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Onverwacht type principal");
        }
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Onjuiste gebruikersnaam of wachtwoord");
        }

        UserDetails userDetails = authenticationService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}