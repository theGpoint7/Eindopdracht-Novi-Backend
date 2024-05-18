package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DesignerRepository designerRepository;

    private User setupTestUser() throws Exception {
        String userJson = """
        {
          "username": "testuser",
          "password": "securepassword",
          "enabled": true,
          "email": "testuser@example.com",
          "firstName": "Test",
          "lastName": "User",
          "dateOfBirth": "1981-11-11",
          "address": "123 Test Street",
          "phoneNo": "084-23432331"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        User user = userRepository.findByUsername("testuser").orElseThrow();

        Designer designer = new Designer();
        designer.setUser(user);
        designer.setStoreName("Test Store");
        designer.setBio("Test Bio");
        designerRepository.save(designer);

        return user;
    }

    private Clothing setupTestProduct() throws Exception {
        String productJson = """
        {
            "productName": "Test sweater",
            "productType": "Clothing",
            "material": "Test material",
            "price": 9.99,
            "inventoryCount": 999,
            "imageUrl": "http://example.com/images/test-sweater.jpg",
            "productDescription": "test description.",
            "clothingSize": "M",
            "color": "Blue",
            "fit": "Regular"
        }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated());

        return (Clothing) productRepository.findByProductName("Test sweater").orElseThrow();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testPlaceOrder_Success() throws Exception {
        User user = setupTestUser();
        Clothing product = setupTestProduct();

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.getItems().add(cartItem);
        cartRepository.save(cart);

        String orderRequestJson = """
        {
            "retrieveCartItems": true
        }
        """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Bestelling succesvol geplaatst."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetOrderDetails_Success() throws Exception {
        User user = setupTestUser();

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        order = orderRepository.save(order);

        mockMvc.perform(get("/orders/" + order.getOrderId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.amount", is(100.0)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetOrderDetails_NotFound() throws Exception {
        setupTestUser();

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bestelling niet gevonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testCancelOrder_Success() throws Exception {
        User user = setupTestUser();

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        orderRepository.save(order);

        mockMvc.perform(put("/orders/" + order.getOrderId() + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bestelling succesvol geannuleerd"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testCancelOrder_NotFound() throws Exception {
        setupTestUser();

        mockMvc.perform(put("/orders/999/cancel"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bestelling niet gevonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testConfirmShipment_Success() throws Exception {
        User user = setupTestUser();

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.CONFIRMED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());
        orderRepository.save(order);

        mockMvc.perform(put("/orders/" + order.getOrderId() + "/confirm-shipment"))
                .andExpect(status().isOk())
                .andExpect(content().string("Verzending bevestigd voor bestelling: " + order.getOrderId()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testConfirmShipment_NotFound() throws Exception {
        setupTestUser();

        mockMvc.perform(put("/orders/999/confirm-shipment"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bestelling niet gevonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetReceiptForOrder_Success() throws Exception {
        User user = setupTestUser();

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.SHIPPED);
        order.setAmount(100.0);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress(user.getAddress());

        Receipt receipt = new Receipt();
        receipt.setDateIssued(LocalDateTime.now());
        receipt.setTotalAmount(100.0);
        receipt.setShippingCost(0.0);
        order.setReceipt(receipt);

        orderRepository.save(order);

        mockMvc.perform(get("/orders/" + order.getOrderId() + "/receipt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalAmount", is(100.0)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void testGetReceiptForOrder_NotFound() throws Exception {
        setupTestUser();

        mockMvc.perform(get("/orders/999/receipt"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bon niet gevonden voor bestelling met ID: 999"));
    }
}
