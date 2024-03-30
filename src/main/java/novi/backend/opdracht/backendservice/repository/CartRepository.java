package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.Cart;
import novi.backend.opdracht.backendservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(User user);
}
