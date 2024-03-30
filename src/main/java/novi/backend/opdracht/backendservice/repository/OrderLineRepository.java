package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    // You can add custom query methods here if needed
}
