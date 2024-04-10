package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Update this method to use a custom query
    @Query("SELECT u FROM User u WHERE u.userCredentials.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
