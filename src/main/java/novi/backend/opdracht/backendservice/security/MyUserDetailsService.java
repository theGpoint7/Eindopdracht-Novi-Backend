//package novi.backend.opdracht.backendservice.security;
//
//import novi.backend.opdracht.backendservice.model.User0;
//import novi.backend.opdracht.backendservice.repository.UserRepository0;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Optional;
//
//public class MyUserDetailsService implements UserDetailsService {
//
//    private final UserRepository0 userRepository;
//
//    public MyUserDetailsService(UserRepository0 userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Use Optional to handle the possibility of the user not being found
//        Optional<User0> userOptional = userRepository.findByUsername(username);
//
//        // Throw an exception if the user is not found, otherwise return the UserDetails
//        User0 user = userOptional.orElseThrow(() ->
//                new UsernameNotFoundException("User not found with username: " + username)
//        );
//
//        return new MyUserDetails(user);
//    }
//}
