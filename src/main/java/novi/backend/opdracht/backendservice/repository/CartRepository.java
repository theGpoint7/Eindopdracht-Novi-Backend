package novi.backend.opdracht.backendservice.repository;

import java.util.Optional;
import novi.backend.opdracht.backendservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserUsername(String username);
}
