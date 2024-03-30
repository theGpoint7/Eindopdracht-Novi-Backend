package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.ProductDto;
import novi.backend.opdracht.backendservice.model.Product;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designer")
public class DesignerController {

    private final ProductRepository productRepository;

    @Autowired
    public DesignerController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PreAuthorize("hasRole('DESIGNER')")
    @PostMapping("/products")
    public ResponseEntity<String> uploadProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInventoryCount(productDto.getInventoryCount());
        product.setImageUrl(productDto.getImageUrl());

        productRepository.save(product);

        return ResponseEntity.ok("Product uploaded successfully");
    }
}
