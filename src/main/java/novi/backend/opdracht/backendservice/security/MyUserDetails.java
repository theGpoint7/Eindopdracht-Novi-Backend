//package novi.backend.opdracht.backendservice.security;
//
//import novi.backend.opdracht.backendservice.model.Role;
//import novi.backend.opdracht.backendservice.model.User0;
//import novi.backend.opdracht.backendservice.model.UserCredentials;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class MyUserDetails implements UserDetails {
//    private final User0 user;
//    private final UserCredentials userCredentials; // Add this line
//
//    public MyUserDetails(User0 user) {
//        this.user = user;
//        this.userCredentials = user.getUserCredentials(); // Initialize userCredentials here
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (Role role : user.getRoles()) {
//            authorities.add(new SimpleGrantedAuthority(role.getRolename()));
//        }
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return userCredentials.getPasswordHash(); // Get passwordHash from userCredentials
//    }
//
//    @Override
//    public String getUsername() {
//        return userCredentials.getUsername(); // Get username from userCredentials
//    }
//   @Override
//    public boolean isAccountNonExpired() { return true; }
//
//   @Override
//    public boolean isAccountNonLocked() { return true; }
//
//    @Override
//    public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public boolean isEnabled() { return true; }
//
//}
