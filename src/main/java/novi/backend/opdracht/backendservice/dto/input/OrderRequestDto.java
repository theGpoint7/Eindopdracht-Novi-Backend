package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class OrderRequestDto {

    @NotNull(message = "retrieveCartItems mag niet null zijn")
    private boolean retrieveCartItems;

    @Size(min = 1, message = "winkelwagen mag niet leeg zijn")
    private List<CartItemInputDto> cartItems;

    public OrderRequestDto() {
    }

    public OrderRequestDto(boolean retrieveCartItems, List<CartItemInputDto> cartItems) {
        this.retrieveCartItems = retrieveCartItems;
        this.cartItems = cartItems;
    }

    public boolean isRetrieveCartItems() {
        return retrieveCartItems;
    }

    public void setRetrieveCartItems(boolean retrieveCartItems) {
        this.retrieveCartItems = retrieveCartItems;
    }

    public List<CartItemInputDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemInputDto> cartItems) {
        this.cartItems = cartItems;
    }
}
