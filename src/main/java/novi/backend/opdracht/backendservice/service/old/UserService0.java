//package novi.backend.opdracht.backendservice.service;
//
//import novi.backend.opdracht.backendservice.dto.DesignerRequestDto;
//import novi.backend.opdracht.backendservice.dto.UserDto;
//import novi.backend.opdracht.backendservice.dto.UserRegistrationDto;
//import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
//import novi.backend.opdracht.backendservice.model.Role;
//import novi.backend.opdracht.backendservice.model.User0;
//import novi.backend.opdracht.backendservice.model.UserCredentials;
//import novi.backend.opdracht.backendservice.model.DesignerRequest;
//import novi.backend.opdracht.backendservice.model.RequestStatus;
//import novi.backend.opdracht.backendservice.repository.DesignerRequestRepository;
//import novi.backend.opdracht.backendservice.repository.RoleRepository;
//import novi.backend.opdracht.backendservice.repository.UserRepository0;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class UserService0 {
//
//    private final UserRepository0 userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final DesignerRequestRepository designerRequestRepository;
//
//    public UserService0(UserRepository0 userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, DesignerRequestRepository designerRequestRepository) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.designerRequestRepository = designerRequestRepository;
//    }
//
//    public UserDto getUser(Long userId){
//        User0 user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        UserDto userDto = new UserDto();
//        userDto.setUserId(user.getUserId());
//        userDto.setFirstName(user.getFirstName());
//        userDto.setLastName(user.getLastName());
//        userDto.setEmail(user.getEmail());
//        userDto.setAddress(user.getAddress());
//        userDto.setPhoneNo(user.getPhoneNo());
//
//        return userDto;
//    }
//
//    public Long createUser(UserRegistrationDto registrationDto) {
//        UserDto userDto = registrationDto.getUser();
//
//        User0 newUser = new User0();
//        UserCredentials credentials = new UserCredentials();
//
//        credentials.setUsername(registrationDto.getUsername());
//        credentials.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
//        newUser.setUserCredentials(credentials);
//
//        newUser.setFirstName(userDto.getFirstName());
//        newUser.setLastName(userDto.getLastName());
//        newUser.setEmail(userDto.getEmail());
//        newUser.setAddress(userDto.getAddress());
//        newUser.setPhoneNo(userDto.getPhoneNo());
//
//        Set<Role> userRoles = new HashSet<>();
//        for (String roleName : userDto.getRoles()) {
//            String fullRoleName = "ROLE_" + roleName; // Prepending "ROLE_" to match the database entries
//            roleRepository.findByRolename(fullRoleName).ifPresent(userRoles::add);
//        }
//        newUser.setRoles(userRoles);
//
//        userRepository.save(newUser);
//
//        return newUser.getUserId(); // Return the ID of the newly created user
//    }
//
//    public void updateUser(Long userId, UserDto userDto) {
//        Optional<User0> optionalUser = userRepository.findById(userId);
//        optionalUser.ifPresent(user -> {
//            user.setFirstName(userDto.getFirstName());
//            user.setLastName(userDto.getLastName());
//            user.setEmail(userDto.getEmail());
//            user.setAddress(userDto.getAddress());
//            user.setPhoneNo(userDto.getPhoneNo());
//            userRepository.save(user);
//        });
//    }
//
//
//    public Optional<User0> findUserById(Long id) {
//        return userRepository.findById(id);
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//
//    public Iterable<User0> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public void addRoleToUser(Long userId, String roleName) {
//        Optional<User0> optionalUser = userRepository.findById(userId);
//        optionalUser.ifPresent(user -> {
//            String fullRoleName = "ROLE_" + roleName; // Prepending "ROLE_" to match the database entries
//            roleRepository.findByRolename(fullRoleName).ifPresent(role -> {
//                user.getRoles().add(role);
//                userRepository.save(user);
//            });
//        });
//    }
//
//    public void removeRoleFromUser(Long userId, String roleName) {
//        Optional<User0> optionalUser = userRepository.findById(userId);
//        optionalUser.ifPresent(user -> {
//            String fullRoleName = "ROLE_" + roleName; // Prepending "ROLE_" to match the database entries
//            roleRepository.findByRolename(fullRoleName).ifPresent(role -> {
//                user.getRoles().remove(role);
//                userRepository.save(user);
//            });
//        });
//    }
//
//    public boolean doesUsernameExist(String username) {
//        return userRepository.existsByUserCredentials_Username(username);
//    }
//
//    public Long submitDesignerRequest(Long userId, DesignerRequestDto requestDto) {
//        Optional<User0> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()) {
//            throw new IllegalArgumentException("User not found");
//        }
//        User0 user = userOptional.get();
//
//        DesignerRequest newRequest = new DesignerRequest();
//        newRequest.setUser(user);
//        newRequest.setStatus(RequestStatus.PENDING);
//        newRequest.setDateOfRequest(LocalDate.now());
//        newRequest.setKvk(requestDto.getKvk());
//
//        designerRequestRepository.save(newRequest);
//
//        return newRequest.getId(); // Assuming DesignerRequest has an ID field
//    }
//}
