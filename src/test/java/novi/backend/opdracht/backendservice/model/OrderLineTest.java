package novi.backend.opdracht.backendservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineTest {

    private Order order;
    private AbstractProduct product;
    private OrderLine orderLine;

    @BeforeEach
    void setUp() {
        order = new Order();
        product = new Accessory();
        product.setPrice(100.0);
        orderLine = new OrderLine(order, product, 2, 100.0, 0.0);
    }

    @Test
    void testDefaultConstructor() {
        OrderLine orderLine = new OrderLine();
        assertNotNull(orderLine);
    }

    @Test
    void testConstructor() {
        Order order = new Order();
        AbstractProduct product = new Accessory();
        int quantity = 5;
        double price = 200.0;
        double discountAmount = 10.0;

        OrderLine orderLine = new OrderLine(order, product, quantity, price, discountAmount);

        assertEquals(order, orderLine.getOrder());
        assertEquals(product, orderLine.getProduct());
        assertEquals(quantity, orderLine.getQuantity());
        assertEquals(price, orderLine.getPrice());
        assertEquals(discountAmount, orderLine.getDiscountAmount());
    }

    @Test
    void testSetAndGetOrderLineId() {
        Long expectedOrderLineId = 1L;
        orderLine.setOrderLineId(expectedOrderLineId);
        Long actualOrderLineId = orderLine.getOrderLineId();
        assertEquals(expectedOrderLineId, actualOrderLineId);
    }

    @Test
    void testSetAndGetOrder() {
        Order expectedOrder = new Order();
        orderLine.setOrder(expectedOrder);
        Order actualOrder = orderLine.getOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testSetAndGetProduct() {
        AbstractProduct expectedProduct = new Accessory();
        orderLine.setProduct(expectedProduct);
        AbstractProduct actualProduct = orderLine.getProduct();
        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void testSetAndGetQuantity() {
        int expectedQuantity = 10;
        orderLine.setQuantity(expectedQuantity);
        int actualQuantity = orderLine.getQuantity();
        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    void testSetAndGetPrice() {
        double expectedPrice = 150.0;
        orderLine.setPrice(expectedPrice);
        double actualPrice = orderLine.getPrice();
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void testSetAndGetDiscountAmount() {
        double expectedDiscountAmount = 20.0;
        orderLine.setDiscountAmount(expectedDiscountAmount);
        double actualDiscountAmount = orderLine.getDiscountAmount();
        assertEquals(expectedDiscountAmount, actualDiscountAmount);
    }

    @Test
    void testCalculateTotalPriceWithoutPromotion() {
        double expectedTotalPrice = 2 * 100.0;
        double actualTotalPrice = orderLine.calculateTotalPrice();
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }

    @Test
    void testCalculateTotalPriceWithPromotion() {
        Promotion promotion = new Promotion();
        promotion.setPromotionPercentage(10.0);
        product.setPromotion(promotion);

        double expectedTotalPrice = 2 * 100.0 * 0.9; // 10% discount
        double actualTotalPrice = orderLine.calculateTotalPrice();
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }
}
