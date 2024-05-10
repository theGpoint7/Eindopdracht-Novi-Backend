package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.AbstractPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<AbstractPaymentMethod, Long> {

}
