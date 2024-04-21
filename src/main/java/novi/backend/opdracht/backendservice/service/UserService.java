package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.UpdateUserRequest;
import novi.backend.opdracht.backendservice.dto.input.UserRequest;
import novi.backend.opdracht.backendservice.dto.output.UserResponse;
import novi.backend.opdracht.backendservice.exception.*;
import novi.backend.opdracht.backendservice.model.Authority;
import novi.backend.opdracht.backendservice.model.Role;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<UserResponse> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserResponse getUser(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return fromUser(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserRequest userRequest) {
        String username = userRequest.getUsername();
        if (userExists(username)) {
            throw new UsernameExistsException(username);
        }
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(encodedPassword);
        User newUser = userRepository.save(toUser(userRequest));
        addAuthority(newUser.getUsername(), Role.ROLE_USER);
        return newUser.getUsername();
    }

    public void deleteUserAccount(String username, String confirmUsername, String confirmPassword) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new UnauthorizedException("You are not authorized to delete another user's account");
        }
        if (!username.equals(confirmUsername)) {
            throw new UnauthorizedException("Confirmation username does not match the user being deleted");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new UnauthorizedException("Incorrect confirmation password");
        }
        userRepository.deleteById(username);
    }

    public void updateUserInformation(String username, UpdateUserRequest updateUserRequest) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new UnauthorizedException("You are not authorized to update another user's information");
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
        userRepository.save(user);
    }

    public void updateUserPassword(String username, String currentPassword, String newPassword) {
        String currentUser = getCurrentUser();
        if (!currentUser.equals(username)) {
            throw new UnauthorizedException("You are not authorized to update another user's password");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UnauthorizedException("Incorrect current password");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
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
                .filter(a -> a.getAuthority() == role) // Filter based on role enum
                .findAny();
        authorityToRemove.ifPresent(user::removeAuthority);
        userRepository.save(user);
    }

    private UserResponse fromUser(User user) {
        var response = new UserResponse();
        response.setUsername(user.getUsername());
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