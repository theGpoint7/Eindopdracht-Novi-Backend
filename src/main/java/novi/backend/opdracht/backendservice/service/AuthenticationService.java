package novi.backend.opdracht.backendservice.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import novi.backend.opdracht.backendservice.exception.AuthenticationException;

@Service
public class AuthenticationService {

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new AuthenticationException("User not authenticated");
        }
        return userDetails.getUsername();
    }
}