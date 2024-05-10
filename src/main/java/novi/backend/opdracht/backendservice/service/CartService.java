package novi.backend.opdracht.backendservice.service;



import novi.backend.opdracht.backendservice.dto.input.CartItemInputDTO;
import novi.backend.opdracht.backendservice.dto.output.CartOutputDTO;
import novi.backend.opdracht.backendservice.dto.output.CartItemOutputDTO;

import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.CartRepository;

import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getUserCart(String username) {
        return cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new BadRequestException("Cart not found"));
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void addToCart(CartItemInputDTO cartItemInputDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    User user = new User();
                    user.setUsername(username);
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        AbstractProduct product = productRepository.findById(cartItemInputDTO.getProductId())
                .orElseThrow(() -> new BadRequestException("Product not found"));
        if (cartItemInputDTO.getQuantity() > product.getInventoryCount()) {
            throw new BadRequestException("Requested quantity exceeds available inventory count");
        }
        if (cart.getItems().stream().anyMatch(item -> item.getProduct().getProductId().equals(cartItemInputDTO.getProductId()))) {
            throw new BadRequestException("Product already exists in the cart");
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemInputDTO.getQuantity());
        cartItem.setCart(cart);
        cart.getItems().add(cartItem);
        cartRepository.save(cart);
    }

    public void removeFromCart(CartItemInputDTO cartItemInputDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new BadRequestException("Cart not found"));
        AbstractProduct product = productRepository.findById(cartItemInputDTO.getProductId())
                .orElseThrow(() -> new BadRequestException("Product not found"));
        boolean isProductInCart = cart.getItems().stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(cartItemInputDTO.getProductId()));

        if (!isProductInCart) {
            throw new BadRequestException("Product does not exist in the cart");
        }
        cart.getItems().removeIf(item -> item.getProduct().getProductId().equals(cartItemInputDTO.getProductId()));
        cartRepository.save(cart);
    }

    public CartOutputDTO getCartContents() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new BadRequestException("Cart not found"));
        CartOutputDTO cartOutputDTO = new CartOutputDTO();
        cartOutputDTO.setCartId(cart.getCartId());
        cartOutputDTO.setUsername(cart.getUser().getUsername());
        double totalPrice = cart.getItems().stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();
        cartOutputDTO.setTotalPrice(totalPrice);

        List<CartItemOutputDTO> cartItemOutputDTOs = cart.getItems().stream()
                .map(cartItem -> {
                    CartItemOutputDTO cartItemOutputDTO = new CartItemOutputDTO();
                    cartItemOutputDTO.setProductId(cartItem.getProduct().getProductId());
                    cartItemOutputDTO.setProductName(cartItem.getProduct().getProductName());
                    cartItemOutputDTO.setPrice(cartItem.getProduct().getPrice());
                    cartItemOutputDTO.setQuantity(cartItem.getQuantity());
                    return cartItemOutputDTO;
                })
                .collect(Collectors.toList());

        cartOutputDTO.setProducts(cartItemOutputDTOs);

        return cartOutputDTO;
    }

public String getProductNameById(Long productId) {
    AbstractProduct product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("Product not found"));
    return product.getProductName();
}


    public void updateCartItemQuantity(CartItemInputDTO cartItemInputDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new BadRequestException("Cart not found"));
        Optional<CartItem> optionalCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(cartItemInputDTO.getProductId()))
                .findFirst();

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            int newQuantity = cartItemInputDTO.getQuantity();
            if (newQuantity > cartItem.getProduct().getInventoryCount()) {
                throw new BadRequestException("Requested quantity exceeds available inventory count");
            }
            cartItem.setQuantity(newQuantity);
            cartRepository.save(cart);
        } else {
            throw new BadRequestException("Product not found in the cart");
        }
    }
}
