//package novi.backend.opdracht.backendservice.repository;
//
//import novi.backend.opdracht.backendservice.model.User0;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRepository0 extends CrudRepository<User0, Long> {
//    // Update this method to use a custom query
//    @Query("SELECT u FROM User0 u WHERE u.userCredentials.username = :username")
//    Optional<User0> findByUsername(@Param("username") String username);
//
//    // Define a custom query method to check if a user with the given username exists
//    boolean existsByUserCredentials_Username(String username);
//}
