package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.dto.CartItemDto;
import novi.backend.opdracht.backendservice.dto.CartTransferDto;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getUserId());
        Cart cart = optionalCart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });

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

    @GetMapping("/view")
    public ResponseEntity<?> viewCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.ok().body("Cart is empty.");
        }

        // Create a custom message indicating cart contents
        StringBuilder message = new StringBuilder("Your cart contains:");
        for (CartItem item : cart.getItems()) {
            message.append("\n - ").append(item.getProduct().getName()).append(" with quantity ").append(item.getQuantity());
        }

        return ResponseEntity.ok().body(message.toString());
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody CartItemDto updatedItemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty.");
        }

        // Find the specific cart item to update
        Optional<CartItem> cartItemOptional = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(updatedItemDto.getProductId()))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(updatedItemDto.getQuantity());
            cartRepository.save(cart);
            return ResponseEntity.ok().body("Cart item quantity updated.");
        } else {
            return ResponseEntity.badRequest().body("Cart item not found.");
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty.");
        }

        // Clear all items from the cart
        cart.getItems().clear();
        cartRepository.save(cart);
        return ResponseEntity.ok().body("Cart cleared.");
    }

    // Calculate Total
    @GetMapping("/total")
    public ResponseEntity<?> calculateTotal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        Cart cart = user.getCart();
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.ok().body("Cart is empty.");
        }

        // Calculate the total price of all items in the cart
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice())
                .reduce();

        return ResponseEntity.ok().body("Total Price: " + total);
    }
}
