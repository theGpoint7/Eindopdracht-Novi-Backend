package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.UserDto;
import novi.backend.opdracht.backendservice.model.Role;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.RoleRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RestController
public class UserController {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public UserController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    @PostMapping("/users")
    public String createUser(@RequestBody UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.username);
        newUser.setPassword(encoder.encode(userDto.password));

        List<Role> userRoles = new ArrayList<>();
        for (String roleName : userDto.roles) {
            Optional<Role> or = roleRepo.findById("ROLE_" + roleName);
            userRoles.add(or.get());
        }

        newUser.setRoles(userRoles);

        userRepo.save(newUser);

        return "Done";
    }
}
