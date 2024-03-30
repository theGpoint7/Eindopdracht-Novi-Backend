package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}