package novi.backend.opdracht.backendservice.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "retrieveCartItems mag niet null zijn")
    private boolean retrieveCartItems;

    @Size(min = 1, message = "winkelwagen mag niet leeg zijn")
    private List<CartItemInputDTO> cartItems;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(boolean retrieveCartItems, List<CartItemInputDTO> cartItems) {
        this.retrieveCartItems = retrieveCartItems;
        this.cartItems = cartItems;
    }

    public boolean isRetrieveCartItems() {
        return retrieveCartItems;
    }

    public void setRetrieveCartItems(boolean retrieveCartItems) {
        this.retrieveCartItems = retrieveCartItems;
    }

    public List<CartItemInputDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemInputDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
