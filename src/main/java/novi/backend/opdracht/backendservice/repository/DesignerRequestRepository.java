package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.DesignerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DesignerRequestRepository extends JpaRepository<DesignerRequest, Long> {
    List<DesignerRequest> findByUserUsername(String username);
}
