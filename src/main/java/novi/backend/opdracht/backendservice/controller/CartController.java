package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.CartTransferDto;
import novi.backend.opdracht.backendservice.dto.CartItemDto;
import novi.backend.opdracht.backendservice.model.Cart;
import novi.backend.opdracht.backendservice.model.CartItem;
import novi.backend.opdracht.backendservice.model.Product;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.CartRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferCart(@RequestBody CartTransferDto cartTransferDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        // Merge cart items
        for (CartItemDto itemDto : cartTransferDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId()).orElse(null);
            if (product != null) {
                // Check if the product is already in the cart
                Optional<CartItem> existingItemOptional = cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(product.getId()))
                        .findFirst();

                if (existingItemOptional.isPresent()) {
                    // Product is already in the cart, update quantity
                    CartItem existingItem = existingItemOptional.get();
                    existingItem.setQuantity(existingItem.getQuantity() + itemDto.getQuantity());
                } else {
                    // Product is not in the cart, add as new item
                    CartItem cartItem = new CartItem();
                    cartItem.setCart(cart);
                    cartItem.setProduct(product);
                    cartItem.setQuantity(itemDto.getQuantity());
                    cart.getItems().add(cartItem);
                }
            }
        }

        cartRepository.save(cart);
        return ResponseEntity.ok().body("Cart transferred and merged successfully.");
    }

}
