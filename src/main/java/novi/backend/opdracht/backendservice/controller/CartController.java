package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.CartItemInputDto;
import novi.backend.opdracht.backendservice.dto.output.CartOutputDto;
import novi.backend.opdracht.backendservice.service.CartService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    private final CartService cartService;
    private final ValidationService validationService;

    public CartController(CartService cartService, ValidationService validationService) {
        this.cartService = cartService;
        this.validationService = validationService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody @Valid CartItemInputDto cartItemInputDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String productName = cartService.getProductNameById(cartItemInputDTO.getProductId());
        cartService.addToCart(cartItemInputDTO);
        String message = "Product '" + productName + "' toegevoegd aan winkelwagen.";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody @Valid CartItemInputDto cartItemInputDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        cartService.removeFromCart(cartItemInputDTO);
        return ResponseEntity.ok("Product verwijderd uit winkelwagen.");
    }

    @GetMapping
    public ResponseEntity<CartOutputDto> getCartContents() {
        CartOutputDto cartContents = cartService.getCartContents();
        return ResponseEntity.ok(cartContents);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItemQuantity(@RequestBody @Valid CartItemInputDto cartItemInputDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        cartService.updateCartItemQuantity(cartItemInputDTO);
        return ResponseEntity.ok("Producthoeveelheid in winkelwagen succesvol bijgewerkt.");
    }
}
