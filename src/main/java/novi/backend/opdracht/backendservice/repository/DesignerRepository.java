package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DesignerRepository extends JpaRepository<Designer, Long> {
    Optional<Designer> findByUserUsername(String username);
}