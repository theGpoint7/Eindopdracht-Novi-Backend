package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.UpdateUserRequest;
import novi.backend.opdracht.backendservice.dto.input.UserRequest;
import novi.backend.opdracht.backendservice.dto.output.FilteredUserResponse;
import novi.backend.opdracht.backendservice.dto.output.UserResponse;
import novi.backend.opdracht.backendservice.exception.*;
import novi.backend.opdracht.backendservice.model.Authority;
import novi.backend.opdracht.backendservice.model.Role;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new AuthenticationException("User not authenticated");
        }
        return userDetails.getUsername();
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::fromUser)
                .collect(Collectors.toList());
    }

    public List<FilteredUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToFullUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserResponse(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return fromUser(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public FilteredUserResponse getFilteredUserResponse(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return mapToFilteredUserResponse(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private FilteredUserResponse mapToFilteredUserResponse(User user) {
        var response = new FilteredUserResponse();
        response.setUsername(user.getUsername());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        return response;
    }


    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public ResponseEntity<?> getUser(String username) {
        String currentUser = getCurrentUser();
        if (currentUser.equals(username)) {
            var fullUserResponse = getUser(username);
            return ResponseEntity.ok().body(fullUserResponse);
        } else {
            List<FilteredUserResponse> allUsers = getAllUsers();
            Optional<FilteredUserResponse> user = allUsers.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();
            if (user.isPresent()) {
                return ResponseEntity.ok().body(user.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }


    public String createUser(UserRequest userRequest) {
        if (userExists(userRequest.getUsername())) {
            throw new UsernameExistsException("Username already exists: " + userRequest.getUsername());
        }
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(encodedPassword);
        userRequest.setUserCreatedOn(LocalDate.now());
        User newUser = toUser(userRequest);
        newUser.setEnabledAccount(true);
        newUser = userRepository.save(newUser);
        addAuthority(newUser.getUsername(), Role.ROLE_CUSTOMER);

        return newUser.getUsername();
    }

    public void deleteUserAccount(String username, String confirmUsername, String confirmPassword) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new AuthorizationServiceException("You are not authorized to delete another user's account");
        }
        if (!username.equals(confirmUsername)) {
            throw new AuthorizationServiceException("Confirmation username does not match the user being deleted");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new AuthorizationServiceException("Incorrect confirmation password");
        }
        userRepository.deleteById(username);
    }

    public void disableUserAccount(String username, String confirmUsername, String confirmPassword) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new AuthorizationServiceException("You are not authorized to disable another user's account");
        }
        if (!username.equals(confirmUsername)) {
            throw new AuthorizationServiceException("Confirmation username does not match the user being disabled");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new AuthorizationServiceException("Incorrect confirmation password");
        }
        user.setEnabledAccount(false);
        removeRoles(user);
        userRepository.save(user);
    }


    private void removeRoles(User user) {
        removeAuthority(user.getUsername(), Role.ROLE_CUSTOMER);
        removeAuthority(user.getUsername(), Role.ROLE_DESIGNER);
    }

    public UserResponse updateUserInformation(String username, UpdateUserRequest updateUserRequest) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new AuthorizationServiceException("You are not authorized to update another user's information");
        }
        if (!userExists(username)) {
            throw new UsernameNotFoundException(username);
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(updateUserRequest.getDateOfBirth());
        }
        if (updateUserRequest.getAddress() != null) {
            user.setAddress(updateUserRequest.getAddress());
        }
        if (updateUserRequest.getPhoneNo() != null) {
            user.setPhoneNo(updateUserRequest.getPhoneNo());
        }
        User updatedUser = userRepository.save(user);
        return fromUser(updatedUser);
    }

    public UserResponse updateUserPassword(String username, String currentPassword, String newPassword) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new AuthorizationServiceException("You are not authorized to update another user's password");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthorizationServiceException("Incorrect current password");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        User updatedUser = userRepository.save(user);
        return fromUser(updatedUser);
    }


    public Set<Role> getAuthorities(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toSet());
    }

    public void addAuthority(String username, Role role) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException(username);
        }

        try {
            User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
            user.addAuthority(new Authority(username, role));
            userRepository.save(user);
        } catch (Exception ex) {
            throw new BadRequestException("Error adding authority for user: " + username);
        }
    }

    public void removeAuthority(String username, Role role) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Optional<Authority> authorityToRemove = user.getAuthorities()
                .stream()
                .filter(a -> a.getAuthority() == role)
                .findAny();
        authorityToRemove.ifPresent(user::removeAuthority);
        userRepository.save(user);
    }



    private UserResponse fromUser(User user) {
        var response = new UserResponse();
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

    private FilteredUserResponse mapToFullUserResponse(User user) {
        var response = new FilteredUserResponse();
        response.setUsername(user.getUsername());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        return response;
    }

    private User toUser(UserRequest userRequest) {
        var user = new User();
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
}