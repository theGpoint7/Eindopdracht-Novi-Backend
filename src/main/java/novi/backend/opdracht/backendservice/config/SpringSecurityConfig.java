package novi.backend.opdracht.backendservice.config;

import novi.backend.opdracht.backendservice.exception.CustomAccessDeniedHandler;
import novi.backend.opdracht.backendservice.filter.JwtRequestFilter;
import novi.backend.opdracht.backendservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http, AccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.GET,"/users").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET,"/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN", "CUSTOMER")

                                .requestMatchers(HttpMethod.POST, "/designerrequest/request").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/designerrequest/request/{username}").hasRole("CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/designerrequest/update").hasRole("DESIGNER")
                                .requestMatchers(HttpMethod.GET, "/designerrequest/requests/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/designerrequest/requests/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/products").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/products").hasRole("DESIGNER")
                                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("DESIGNER")
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("DESIGNER")

                                .requestMatchers(HttpMethod.POST, "/cart/**").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/cart/**").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/cart").hasAnyRole("DESIGNER", "CUSTOMER")

                                .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/orders/{orderId}").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/orders/user/{username}").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT, "/orders/{orderId}/cancel").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/orders/{orderId}/receipt").hasAnyRole("DESIGNER", "CUSTOMER")

                                .requestMatchers(HttpMethod.POST, "/payments/process/{orderId}").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/payments/confirm/{orderId}").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.PUT, "/orders/{orderId}/confirm-shipment").hasRole("DESIGNER")

                                .requestMatchers(HttpMethod.POST, "/feedback").hasRole("CUSTOMER")

                                .requestMatchers(HttpMethod.GET, "/designers").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/designers/{designerId}").hasAnyRole("DESIGNER", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST, "/designers/{designerId}/promotion").hasRole("DESIGNER")
                                .requestMatchers(HttpMethod.GET, "/designers/{designerId}/promotions").hasRole("DESIGNER")
                                .requestMatchers(HttpMethod.PUT, "/designers/{designerId}/promotion/apply").hasRole("DESIGNER")
                                .requestMatchers(HttpMethod.GET, "/designers/{designerId}/sales").hasRole("DESIGNER")

                                .requestMatchers(HttpMethod.GET, "users/{username}/authorities").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "users/{username}/authorities").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "users/{username}/authorities/**").hasRole("ADMIN")

                                .requestMatchers("/authenticated").authenticated()
                                .requestMatchers("/authenticate").permitAll()
                                .anyRequest().denyAll()

                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
