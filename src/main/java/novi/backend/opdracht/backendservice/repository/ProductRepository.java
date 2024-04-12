package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
