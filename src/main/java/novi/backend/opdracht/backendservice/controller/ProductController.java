package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDTO;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDTO;
import novi.backend.opdracht.backendservice.service.FeedbackService;
import novi.backend.opdracht.backendservice.service.ProductService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    private final ValidationService validationService;

    public ProductController(ProductService productService, FeedbackService feedbackService, ValidationService validationService) {
        this.productService = productService;
        this.feedbackService = feedbackService;
        this.validationService = validationService;
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
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductInputDTO productInputDTO, BindingResult result) {
        if (result.hasFieldErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        ProductOutputDTO createdProduct = productService.createProduct(productInputDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getProductId())
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestBody @Valid ProductInputDTO productInputDTO, BindingResult result) {
        if (result.hasFieldErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        ProductOutputDTO updatedProduct = productService.updateProduct(productId, productInputDTO);
        return ResponseEntity.ok(updatedProduct);
    }
}