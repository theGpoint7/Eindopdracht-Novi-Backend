package novi.backend.opdracht.backendservice.repository;

import novi.backend.opdracht.backendservice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private Order order1;
    private Order order2;
    private User user;

    @BeforeEach
    void setUp() {
        // Create and save the User entity
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user = userRepository.save(user); // Save the user entity first

        // Create Order entities and set the saved User entity
        order1 = new Order();
        order1.setUser(user);
        order1.setDesignerId(1L);
        order1.setAmount(100.0);
        order1.setOrderDateTime(LocalDateTime.now());
        order1.setShippingAddress("123 Test Street");
        order1.setOrderStatus(OrderStatus.PENDING);

        order2 = new Order();
        order2.setUser(user);
        order2.setDesignerId(1L);
        order2.setAmount(200.0);
        order2.setOrderDateTime(LocalDateTime.now());
        order2.setShippingAddress("456 Test Avenue");
        order2.setOrderStatus(OrderStatus.PENDING);

        // Save Order entities
        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    void testFindByUserUsername() {
        List<Order> orders = orderRepository.findByUserUsername("testuser");
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals("testuser", orders.get(0).getUser().getUsername());
    }

    @Test
    void testFindAllByDesignerId() {
        List<Order> orders = orderRepository.findAllByDesignerId(1L);
        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals(1L, orders.get(0).getDesignerId());
    }
}
