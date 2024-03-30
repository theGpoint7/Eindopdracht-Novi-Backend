package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.Role;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, String> {
    Optional<Role> findByRolename(String rolename);
}