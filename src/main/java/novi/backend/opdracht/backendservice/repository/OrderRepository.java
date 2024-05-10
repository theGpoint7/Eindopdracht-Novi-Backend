package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsername(String username);
    List<Order> findAllByUserDesignerDesignerId(Long designerId);
    List<Order> findAllByDesignerId(Long designerId);
}
