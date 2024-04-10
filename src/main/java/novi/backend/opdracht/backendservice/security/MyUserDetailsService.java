package novi.backend.opdracht.backendservice.security;

import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Use Optional to handle the possibility of the user not being found
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Throw an exception if the user is not found, otherwise return the UserDetails
        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username)
        );

        return new MyUserDetails(user);
    }
}
