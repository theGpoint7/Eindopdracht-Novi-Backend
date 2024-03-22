package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
