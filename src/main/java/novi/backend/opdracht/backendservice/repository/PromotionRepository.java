package novi.backend.opdracht.backendservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import novi.backend.opdracht.backendservice.model.Promotion;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByDesignerDesignerId(Long designerId);
    List<Promotion> findByPromotionName(String promotionName);

    boolean existsByPromotionName(String promotionName);
}
