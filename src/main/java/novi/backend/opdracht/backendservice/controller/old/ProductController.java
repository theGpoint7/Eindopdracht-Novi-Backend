//package novi.backend.opdracht.backendservice.controller;
//
//import jakarta.validation.Valid;
//import novi.backend.opdracht.backendservice.dto.inputdtos.ProductInputDto;
//import novi.backend.opdracht.backendservice.dto.outputdtos.ProductOutputDto;
//import novi.backend.opdracht.backendservice.service.ProductService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ProductOutputDto>> getAllProducts() {
//        List<ProductOutputDto> products = productService.getAllProducts();
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductInputDto productInputDto, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            StringBuilder stringBuilder = new StringBuilder();
//            for (FieldError fieldError : bindingResult.getFieldErrors()){
//                stringBuilder.append(fieldError.getField()).append(": ");
//                stringBuilder.append(fieldError.getDefaultMessage());
//                stringBuilder.append("\n");
//            }
//            return ResponseEntity.badRequest().body(stringBuilder.toString());
//        }
//        ProductOutputDto createdProduct = productService.createProduct(productInputDto);
//        URI uri = URI.create("/api/products/" + createdProduct.id);
//        return ResponseEntity.created(uri).body(createdProduct);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductOutputDto> getProductById(@PathVariable Long id) {
//        ProductOutputDto product = productService.getProductById(id);
//        return new ResponseEntity<>(product, HttpStatus.OK);
//    }
//}
//
//
