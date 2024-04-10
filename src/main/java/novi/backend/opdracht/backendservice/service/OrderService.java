package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.model.CartItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    public BigDecimal calculateShippingCosts(List<CartItem> cartItems) {
        // Your logic to calculate shipping costs based on cart items
        return BigDecimal.valueOf(10.0); // Example: Fixed shipping cost for demonstration
    }

    // Other order-related methods can go here
}
