package novi.backend.opdracht.backendservice.controller;

import novi.backend.opdracht.backendservice.model.Order;
import novi.backend.opdracht.backendservice.model.OrderLine;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        // Here you can do any necessary validations before saving
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok().body(savedOrder);
    }

    @GetMapping("/view/{orderId}")
    public ResponseEntity<?> viewOrder(@PathVariable Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();
        return ResponseEntity.ok().body(order);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();
        // Update order details
        order.setOrderLines(updatedOrder.getOrderLines());
        // You can add more update logic here based on your requirements

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok().body(savedOrder);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        orderRepository.deleteById(orderId);
        return ResponseEntity.ok().body("Order deleted successfully");
    }

    @GetMapping("/total/{orderId}")
    public ResponseEntity<?> calculateTotalPrice(@PathVariable Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();
        BigDecimal total = order.getOrderLines().stream()
                .map(orderLine -> orderLine.getUnitPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok().body("Total Price: " + total);
    }

    @PostMapping("/{orderId}/addOrderLine")
    public ResponseEntity<?> addOrderLineToOrder(@PathVariable Long orderId, @RequestBody OrderLine orderLine) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();
        order.addOrderLine(orderLine);
        orderRepository.save(order);

        return ResponseEntity.ok().body("OrderLine added to Order successfully");
    }

    @DeleteMapping("/{orderId}/orderLines/{orderLineId}")
    public ResponseEntity<?> removeOrderLine(@PathVariable Long orderId, @PathVariable Long orderLineId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        Order order = optionalOrder.get();

        // Call the removeOrderLineById method from the Order class
        order.removeOrderLineById(orderLineId);

        // Save the updated order
        orderRepository.save(order);

        return ResponseEntity.ok().body("OrderLine with ID " + orderLineId + " removed from the order");
    }

    // Getters and Setters
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
