package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.UpdateUserRequest;
import novi.backend.opdracht.backendservice.dto.input.UserRequest;
import novi.backend.opdracht.backendservice.dto.output.FilteredUserResponse;
import novi.backend.opdracht.backendservice.dto.output.UserResponse;

import novi.backend.opdracht.backendservice.exception.UsernameExistsException;
import novi.backend.opdracht.backendservice.model.Authority;
import novi.backend.opdracht.backendservice.model.Role;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public String createUser(UserRequest userRequest) {
        if (userRepository.existsById(userRequest.getUsername())) {
            throw new UsernameExistsException("Gebruikersnaam bestaat al: " + userRequest.getUsername());
        }
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(encodedPassword);
        userRequest.setUserCreatedOn(LocalDate.now());
        User newUser = toUser(userRequest);
        newUser.enableAccount();
        newUser = userRepository.save(newUser);
        addAuthority(newUser.getUsername(), Role.ROLE_CUSTOMER);

        return newUser.getUsername();
    }

    public void deleteUserAccount(String username, String confirmUsername, String confirmPassword) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getUsername().equals(username)) {
            throw new AuthorizationServiceException("U bent niet gemachtigd om het account van een andere gebruiker te verwijderen");
        }
        if (!username.equals(confirmUsername)) {
            throw new AuthorizationServiceException("Bevestigingsgebruikersnaam komt niet overeen met de gebruiker die wordt verwijderd");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new AuthorizationServiceException("Onjuist bevestigingswachtwoord");
        }
        userRepository.deleteById(username);
    }

    public void disableUserAccount(String username, String confirmUsername, String confirmPassword) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getUsername().equals(username)) {
            throw new AuthorizationServiceException("U bent niet gemachtigd om het account van een andere gebruiker te deactiveren");
        }
        if (!username.equals(confirmUsername)) {
            throw new AuthorizationServiceException("Bevestigingsgebruikersnaam komt niet overeen met de gebruiker die wordt gedeactiveerd");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new AuthorizationServiceException("Onjuist bevestigingswachtwoord");
        }
        user.disableAccount();
        removeRoles(user);
        userRepository.save(user);
    }

    private void removeRoles(User user) {
        removeAuthority(user.getUsername(), Role.ROLE_CUSTOMER);
        removeAuthority(user.getUsername(), Role.ROLE_DESIGNER);
    }

    public UserResponse updateUserInformation(String username, UpdateUserRequest updateUserRequest) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getUsername().equals(username)) {
            throw new AuthorizationServiceException("U bent niet gemachtigd om de gegevens van een andere gebruiker bij te werken");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        user.updateInformation(updateUserRequest);
        User updatedUser = userRepository.save(user);
        return fromUser(updatedUser);
    }

    public UserResponse updateUserPassword(String username, String currentPassword, String newPassword) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getUsername().equals(username)) {
            throw new AuthorizationServiceException("U bent niet gemachtigd om het wachtwoord van een andere gebruiker bij te werken");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthorizationServiceException("Onjuist huidig wachtwoord");
        }
        user.updatePassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);
        return fromUser(updatedUser);
    }

    public Set<Role> getAuthorities(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        return user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toSet());
    }

    public void addAuthority(String username, Role role) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        user.addAuthority(new Authority(username, role));
        userRepository.save(user);
    }

    public void removeAuthority(String username, Role role) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        Optional<Authority> authorityToRemove = user.getAuthorities().stream()
                .filter(a -> a.getAuthority() == role)
                .findAny();
        authorityToRemove.ifPresent(user::removeAuthority);
        userRepository.save(user);
    }

    public UserResponse getUserResponse(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        return fromUser(user);
    }

    public FilteredUserResponse getFilteredUserResponse(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
        return mapToFilteredUserResponse(user);
    }

    private UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setUserName(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setAddress(user.getAddress());
        response.setPhoneNo(user.getPhoneNo());
        response.setUserCreatedOn(user.getUserCreatedOn());
        response.setAuthorities(user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet()));
        return response;
    }

    private FilteredUserResponse mapToFilteredUserResponse(User user) {
        FilteredUserResponse response = new FilteredUserResponse();
        response.setUsername(user.getUsername());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        return response;
    }

    private User toUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setAddress(userRequest.getAddress());
        user.setPhoneNo(userRequest.getPhoneNo());
        user.setUserCreatedOn(userRequest.getUserCreatedOn());
        return user;
    }

    public List<FilteredUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToFilteredUserResponse).collect(Collectors.toList());
    }

    public User getCurrentUser() {
        return authenticationService.getCurrentUser();
    }
}
