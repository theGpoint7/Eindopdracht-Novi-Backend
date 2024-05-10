package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.OrderRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.OrderOutputDTO;
import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDTO;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequestDTO orderRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Foutieve invoer bij het plaatsen van de bestelling");
        }
        return orderService.placeOrder(orderRequest);
    }



    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            OrderOutputDTO orderDetails = orderService.getOrderDetails(orderId);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bestelling niet gevonden");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderOutputDTO>> getUserOrders() {
        List<OrderOutputDTO> userOrders = orderService.getUserOrders();
        return ResponseEntity.ok(userOrders);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Bestelling succesvol geannuleerd");
    }

    @PutMapping("/{orderId}/confirm-shipment")
    public ResponseEntity<String> confirmShipment(@PathVariable Long orderId) {
        ResponseEntity<String> response = orderService.confirmShipment(orderId);
        return ResponseEntity.ok("Verzending bevestigd voor bestelling ID: " + orderId);
    }

    @GetMapping("/{orderId}/receipt")
    public ResponseEntity<?> getReceiptForOrder(@PathVariable Long orderId) {
        try {
            ReceiptOutputDTO receiptOutputDTO = orderService.getReceiptForOrder(orderId);
            return ResponseEntity.ok(receiptOutputDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
