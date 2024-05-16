package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.AbstractProduct;
import novi.backend.opdracht.backendservice.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProductProductId (Long productId);
}
