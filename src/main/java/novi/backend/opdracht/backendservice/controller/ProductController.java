package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDTO;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDTO;
import novi.backend.opdracht.backendservice.service.FeedbackService;
import novi.backend.opdracht.backendservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;
    private final FeedbackService feedbackService;

    public ProductController(ProductService productService, FeedbackService feedbackService) {
        this.productService = productService;
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<ProductOutputDTO>> getAllProducts(
            @RequestParam(required = false) Integer schoenmaat,
            @RequestParam(required = false) String kledingmaat,
            @RequestParam(required = false) String kleur,
            @RequestParam(required = false) String winkelnaam) {
        List<ProductOutputDTO> producten = productService.findAllProducts(schoenmaat, kledingmaat, kleur, winkelnaam);

        for (ProductOutputDTO product : producten) {
            List<FeedbackOutputDTO> feedbacks = feedbackService.getFeedbacksByProductId(product.getProductId());
            product.setFeedbacks(feedbacks);
        }

        return ResponseEntity.ok(producten);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductOutputDTO> getProductById(@PathVariable Long productId) {
        ProductOutputDTO product = productService.getProductById(productId);

        List<FeedbackOutputDTO> feedbacks = feedbackService.getFeedbacksByProductId(productId);
        product.setFeedbacks(feedbacks);

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductOutputDTO> createProduct(@RequestBody @Valid ProductInputDTO productInputDTO) {
        ProductOutputDTO createdProduct = productService.createProduct(productInputDTO);
        URI locatie = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getProductId())
                .toUri();
        return ResponseEntity.created(locatie).body(createdProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductOutputDTO> updateProduct(@PathVariable Long productId,
                                                          @RequestBody @Valid ProductInputDTO productInputDTO) {
        ProductOutputDTO updatedProduct = productService.updateProduct(productId, productInputDTO);
        return ResponseEntity.ok(updatedProduct);
    }
}
