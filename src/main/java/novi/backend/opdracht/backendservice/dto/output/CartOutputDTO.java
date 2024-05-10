package novi.backend.opdracht.backendservice.dto.output;

import java.util.List;

public class CartOutputDTO {
    private Long cartId;
    private String username;
    private List<CartItemOutputDTO> products;
    private double totalPrice;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItemOutputDTO> getProducts() {
        return products;
    }

    public void setProducts(List<CartItemOutputDTO> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
