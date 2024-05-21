package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.CartItemInputDto;
import novi.backend.opdracht.backendservice.dto.output.CartOutputDto;
import novi.backend.opdracht.backendservice.dto.output.CartItemOutputDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.CartRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, AuthenticationService authenticationService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.authenticationService = authenticationService;
    }

    public Cart getUserCart() {
        String username = authenticationService.getCurrentUsername();
        return cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new BadRequestException("Winkelwagen niet gevonden"));
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void addToCart(CartItemInputDto cartItemInputDTO) {
        User user = authenticationService.getCurrentUser();
        Cart cart = cartRepository.findByUserUsername(user.getUsername())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        AbstractProduct product = productRepository.findById(cartItemInputDTO.getProductId())
                .orElseThrow(() -> new BadRequestException("Product niet gevonden"));
        if (cartItemInputDTO.getQuantity() > product.getInventoryCount()) {
            throw new BadRequestException("Gevraagde hoeveelheid overschrijdt beschikbare voorraad");
        }
        if (cart.hasProduct(cartItemInputDTO.getProductId())) {
            throw new BadRequestException("Product bestaat al in de winkelwagen");
        }
        CartItem cartItem = new CartItem(cart, product, cartItemInputDTO.getQuantity());
        cart.addItem(cartItem);
        cartRepository.save(cart);
    }

    public void removeFromCart(CartItemInputDto cartItemInputDTO) {
        User user = authenticationService.getCurrentUser();
        Cart cart = cartRepository.findByUserUsername(user.getUsername())
                .orElseThrow(() -> new BadRequestException("Winkelwagen niet gevonden"));
        AbstractProduct product = productRepository.findById(cartItemInputDTO.getProductId())
                .orElseThrow(() -> new BadRequestException("Product niet gevonden"));
        if (!cart.hasProduct(cartItemInputDTO.getProductId())) {
            throw new BadRequestException("Product bestaat niet in de winkelwagen");
        }
        cart.removeItem(cartItemInputDTO.getProductId());
        cartRepository.save(cart);
    }

    public CartOutputDto getCartContents() {
        User user = authenticationService.getCurrentUser();
        Cart cart = cartRepository.findByUserUsername(user.getUsername())
                .orElseThrow(() -> new BadRequestException("Winkelwagen niet gevonden"));
        CartOutputDto cartOutputDTO = new CartOutputDto();
        cartOutputDTO.setCartId(cart.getCartId());
        cartOutputDTO.setUsername(cart.getUser().getUsername());
        double totalPrice = cart.getItems().stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();
        cartOutputDTO.setTotalPrice(totalPrice);

        List<CartItemOutputDto> cartItemOutputDTOs = cart.getItems().stream()
                .map(cartItem -> {
                    CartItemOutputDto cartItemOutputDTO = new CartItemOutputDto();
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
                .orElseThrow(() -> new BadRequestException("Product niet gevonden"));
        return product.getProductName();
    }

    public void updateCartItemQuantity(CartItemInputDto cartItemInputDTO) {
        User user = authenticationService.getCurrentUser();
        Cart cart = cartRepository.findByUserUsername(user.getUsername())
                .orElseThrow(() -> new BadRequestException("Winkelwagen niet gevonden"));
        CartItem cartItem = cart.getItemByProductId(cartItemInputDTO.getProductId());

        if (cartItem == null) {
            throw new BadRequestException("Product niet gevonden in de winkelwagen");
        }

        int newQuantity = cartItemInputDTO.getQuantity();
        if (newQuantity > cartItem.getProduct().getInventoryCount()) {
            throw new BadRequestException("Gevraagde hoeveelheid overschrijdt beschikbare voorraad");
        }

        cartItem.setQuantity(newQuantity);
        cartRepository.save(cart);
    }
}
