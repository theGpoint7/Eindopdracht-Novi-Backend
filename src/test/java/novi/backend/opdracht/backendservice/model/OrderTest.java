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
        Long expectedOrderId = 1L;
        order.setOrderId(expectedOrderId);
        Long actualOrderId = order.getOrderId();
        assertEquals(expectedOrderId, actualOrderId);
    }

    @Test
    public void testSetAndGetUser() {
        User expectedUser = user;
        order.setUser(expectedUser);
        User actualUser = order.getUser();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testSetAndGetDesignerId() {
        Long expectedDesignerId = 1L;
        order.setDesignerId(expectedDesignerId);
        Long actualDesignerId = order.getDesignerId();
        assertEquals(expectedDesignerId, actualDesignerId);
    }

    @Test
    public void testSetAndGetOrderLines() {
        Set<OrderLine> expectedOrderLines = new HashSet<>();
        expectedOrderLines.add(orderLine);
        order.setOrderLines(expectedOrderLines);
        Set<OrderLine> actualOrderLines = order.getOrderLines();
        assertEquals(expectedOrderLines, actualOrderLines);
    }

    @Test
    public void testSetAndGetPaymentMethod() {
        AbstractPaymentMethod expectedPaymentMethod = new AbstractPaymentMethod() {
            @Override
            public String getPaymentMethodType() {
                return "TEST_METHOD";
            }
        };
        order.setPaymentMethod(expectedPaymentMethod);
        AbstractPaymentMethod actualPaymentMethod = order.getPaymentMethod();
        assertEquals(expectedPaymentMethod, actualPaymentMethod);
    }


    @Test
    public void testSetAndGetAmount() {
        double expectedAmount = 100.0;
        order.setAmount(expectedAmount);
        double actualAmount = order.getAmount();
        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void testSetAndGetOrderDateTime() {
        LocalDateTime expectedOrderDateTime = LocalDateTime.now();
        order.setOrderDateTime(expectedOrderDateTime);
        LocalDateTime actualOrderDateTime = order.getOrderDateTime();
        assertEquals(expectedOrderDateTime, actualOrderDateTime);
    }

    @Test
    public void testSetAndGetShippingAddress() {
        String expectedShippingAddress = "New Address";
        order.setShippingAddress(expectedShippingAddress);
        String actualShippingAddress = order.getShippingAddress();
        assertEquals(expectedShippingAddress, actualShippingAddress);
    }

    @Test
    public void testSetAndGetOrderStatus() {
        OrderStatus expectedOrderStatus = OrderStatus.CONFIRMED;
        order.setOrderStatus(expectedOrderStatus);
        OrderStatus actualOrderStatus = order.getOrderStatus();
        assertEquals(expectedOrderStatus, actualOrderStatus);
    }

    @Test
    public void testSetAndGetReceipt() {
        Receipt expectedReceipt = new Receipt();
        order.setReceipt(expectedReceipt);
        Receipt actualReceipt = order.getReceipt();
        assertEquals(expectedReceipt, actualReceipt);
    }

    @Test
    public void testSetAndGetPaymentStatus() {
        PaymentStatus expectedPaymentStatus = PaymentStatus.CONFIRMED;
        order.setPaymentStatus(expectedPaymentStatus);
        PaymentStatus actualPaymentStatus = order.getPaymentStatus();
        assertEquals(expectedPaymentStatus, actualPaymentStatus);
    }

    @Test
    public void testUpdateOrderStatus() {
        OrderStatus expectedOrderStatus = OrderStatus.CONFIRMED;
        order.updateOrderStatus(expectedOrderStatus);
        OrderStatus actualOrderStatus = order.getOrderStatus();
        assertEquals(expectedOrderStatus, actualOrderStatus);
    }

    @Test
    public void testUpdateOrderStatusThrowsExceptionIfShipped() {
        order.setOrderStatus(OrderStatus.SHIPPED);
        assertThrows(IllegalStateException.class, () -> order.updateOrderStatus(OrderStatus.CANCELED));
    }

    @Test
    public void testUpdateOrderStatusThrowsExceptionIfConfirmed() {
        order.setOrderStatus(OrderStatus.CONFIRMED);
        assertThrows(IllegalStateException.class, () -> order.updateOrderStatus(OrderStatus.CANCELED));
    }

    @Test
    public void testIsOwnedBy() {
        assertTrue(order.isOwnedBy("testuser"));
        assertFalse(order.isOwnedBy("anotheruser"));
    }

    @Test
    public void testCalculateTotalAmount() {
        double actualTotalAmount = order.calculateTotalAmount();
        assertEquals(180.0, actualTotalAmount);
    }

    @Test
    public void testAddOrderLine() {
        OrderLine newOrderLine = new OrderLine();
        newOrderLine.setProduct(product);
        newOrderLine.setQuantity(1);
        newOrderLine.setPrice(50.0);
        order.addOrderLine(newOrderLine);
        assertTrue(order.getOrderLines().contains(newOrderLine));
    }

    @Test
    public void testContainsProductsByDesigner() {
        assertTrue(order.containsProductsByDesigner("testuser"));
    }

    @Test
    public void testCalculateTotalDiscount() {
        double actualTotalDiscount = order.calculateTotalDiscount();
        assertEquals(20.0, actualTotalDiscount);
    }
}
