package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.CartItemInputDTO;
import novi.backend.opdracht.backendservice.dto.output.CartOutputDTO;
import novi.backend.opdracht.backendservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody @Valid CartItemInputDTO cartItemInputDTO) {
        String productName = cartService.getProductNameById(cartItemInputDTO.getProductId());
        cartService.addToCart(cartItemInputDTO);
        String message = "Product '" + productName + "' added to cart.";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody @Valid CartItemInputDTO cartItemInputDTO) {
        cartService.removeFromCart(cartItemInputDTO);
        return ResponseEntity.ok("Product removed from cart.");
    }

    @GetMapping
    public ResponseEntity<CartOutputDTO> getCartContents() {
        CartOutputDTO cartContents = cartService.getCartContents();
        return ResponseEntity.ok(cartContents);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItemQuantity(@RequestBody @Valid CartItemInputDTO cartItemInputDTO) {
        cartService.updateCartItemQuantity(cartItemInputDTO);
        return ResponseEntity.ok("Product quantity in cart updated successfully.");
    }
}
