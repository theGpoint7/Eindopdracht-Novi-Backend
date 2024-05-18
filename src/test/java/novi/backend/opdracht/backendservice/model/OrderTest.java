package novi.backend.opdracht.backendservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order order;
    private User user;
    private OrderLine orderLine;
    private AbstractProduct product;
    private Designer designer;
    private Promotion promotion;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");

        designer = new Designer();
        designer.setDesignerId(1L);
        designer.setUser(user);

        product = new AbstractProduct() {
            @Override
            public String getProductType() {
                return "TestType";
            }
        };
        product.setProductId(1L);
        product.setPrice(100.0);
        product.setDesigner(designer);

        promotion = new Promotion();
        promotion.setPromotionPercentage(10.0);
        product.setPromotion(promotion);

        orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(2);
        orderLine.setPrice(100.0);

        order = new Order();
        order.setUser(user);
        order.setOrderDateTime(LocalDateTime.now());
        order.setShippingAddress("Test Address");
        order.setOrderStatus(OrderStatus.PENDING);
        order.addOrderLine(orderLine);
    }

    @Test
    public void testSetAndGetOrderId() {
        // Arrange
        Long expectedOrderId = 1L;

        // Act
        order.setOrderId(expectedOrderId);
        Long actualOrderId = order.getOrderId();

        // Assert
        assertEquals(expectedOrderId, actualOrderId);
    }

    @Test
    public void testSetAndGetUser() {
        // Arrange
        User expectedUser = user;

        // Act
        order.setUser(expectedUser);
        User actualUser = order.getUser();

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testSetAndGetDesignerId() {
        // Arrange
        Long expectedDesignerId = 1L;

        // Act
        order.setDesignerId(expectedDesignerId);
        Long actualDesignerId = order.getDesignerId();

        // Assert
        assertEquals(expectedDesignerId, actualDesignerId);
    }

    @Test
    public void testSetAndGetOrderLines() {
        // Arrange
        Set<OrderLine> expectedOrderLines = new HashSet<>();
        expectedOrderLines.add(orderLine);

        // Act
        order.setOrderLines(expectedOrderLines);
        Set<OrderLine> actualOrderLines = order.getOrderLines();

        // Assert
        assertEquals(expectedOrderLines, actualOrderLines);
    }

    @Test
    public void testSetAndGetPaymentMethod() {
        // Arrange
        AbstractPaymentMethod expectedPaymentMethod = new AbstractPaymentMethod() {
            @Override
            public String getPaymentMethodType() {
                return "TEST_METHOD";
            }
        };

        // Act
        order.setPaymentMethod(expectedPaymentMethod);
        AbstractPaymentMethod actualPaymentMethod = order.getPaymentMethod();

        // Assert
        assertEquals(expectedPaymentMethod, actualPaymentMethod);
    }


    @Test
    public void testSetAndGetAmount() {
        // Arrange
        double expectedAmount = 100.0;

        // Act
        order.setAmount(expectedAmount);
        double actualAmount = order.getAmount();

        // Assert
        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void testSetAndGetOrderDateTime() {
        // Arrange
        LocalDateTime expectedOrderDateTime = LocalDateTime.now();

        // Act
        order.setOrderDateTime(expectedOrderDateTime);
        LocalDateTime actualOrderDateTime = order.getOrderDateTime();

        // Assert
        assertEquals(expectedOrderDateTime, actualOrderDateTime);
    }

    @Test
    public void testSetAndGetShippingAddress() {
        // Arrange
        String expectedShippingAddress = "New Address";

        // Act
        order.setShippingAddress(expectedShippingAddress);
        String actualShippingAddress = order.getShippingAddress();

        // Assert
        assertEquals(expectedShippingAddress, actualShippingAddress);
    }

    @Test
    public void testSetAndGetOrderStatus() {
        // Arrange
        OrderStatus expectedOrderStatus = OrderStatus.CONFIRMED;

        // Act
        order.setOrderStatus(expectedOrderStatus);
        OrderStatus actualOrderStatus = order.getOrderStatus();

        // Assert
        assertEquals(expectedOrderStatus, actualOrderStatus);
    }

    @Test
    public void testSetAndGetReceipt() {
        // Arrange
        Receipt expectedReceipt = new Receipt();

        // Act
        order.setReceipt(expectedReceipt);
        Receipt actualReceipt = order.getReceipt();

        // Assert
        assertEquals(expectedReceipt, actualReceipt);
    }

    @Test
    public void testSetAndGetPaymentStatus() {
        // Arrange
        PaymentStatus expectedPaymentStatus = PaymentStatus.CONFIRMED;

        // Act
        order.setPaymentStatus(expectedPaymentStatus);
        PaymentStatus actualPaymentStatus = order.getPaymentStatus();

        // Assert
        assertEquals(expectedPaymentStatus, actualPaymentStatus);
    }

    @Test
    public void testUpdateOrderStatus() {
        // Arrange
        OrderStatus expectedOrderStatus = OrderStatus.CONFIRMED;

        // Act
        order.updateOrderStatus(expectedOrderStatus);
        OrderStatus actualOrderStatus = order.getOrderStatus();

        // Assert
        assertEquals(expectedOrderStatus, actualOrderStatus);
    }

    @Test
    public void testUpdateOrderStatusThrowsExceptionIfShipped() {
        // Arrange
        order.setOrderStatus(OrderStatus.SHIPPED);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> order.updateOrderStatus(OrderStatus.CANCELED));
    }

    @Test
    public void testUpdateOrderStatusThrowsExceptionIfConfirmed() {
        // Arrange
        order.setOrderStatus(OrderStatus.CONFIRMED);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> order.updateOrderStatus(OrderStatus.CANCELED));
    }

    @Test
    public void testIsOwnedBy() {
        // Act & Assert
        assertTrue(order.isOwnedBy("testuser"));
        assertFalse(order.isOwnedBy("anotheruser"));
    }

    @Test
    public void testCalculateTotalAmount() {
        // Act
        double actualTotalAmount = order.calculateTotalAmount();

        // Assert
        assertEquals(180.0, actualTotalAmount);
    }

    @Test
    public void testAddOrderLine() {
        // Arrange
        OrderLine newOrderLine = new OrderLine();
        newOrderLine.setProduct(product);
        newOrderLine.setQuantity(1);
        newOrderLine.setPrice(50.0);

        // Act
        order.addOrderLine(newOrderLine);

        // Assert
        assertTrue(order.getOrderLines().contains(newOrderLine));
    }

    @Test
    public void testContainsProductsByDesigner() {
        // Act & Assert
        assertTrue(order.containsProductsByDesigner("testuser"));
    }

    @Test
    public void testCalculateTotalDiscount() {
        // Act
        double actualTotalDiscount = order.calculateTotalDiscount();

        // Assert
        assertEquals(20.0, actualTotalDiscount);
    }
}
