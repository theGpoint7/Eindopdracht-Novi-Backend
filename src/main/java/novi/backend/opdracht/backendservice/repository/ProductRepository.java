package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.AbstractProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<AbstractProduct, Long> {
    boolean existsByProductName(String productName);
    Optional<AbstractProduct> findByProductName(String productName);

    @Query("SELECT p FROM AbstractProduct p LEFT JOIN FETCH p.designer WHERE p.designer.storeName = :storeName")
    List<AbstractProduct> findByDesignerStoreName(@Param("storeName") String storeName);
}
