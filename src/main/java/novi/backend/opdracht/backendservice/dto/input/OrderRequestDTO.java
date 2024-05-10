package novi.backend.opdracht.backendservice.dto.input;

import java.util.List;

public class OrderRequestDTO {
    private boolean retrieveCartItems;
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
