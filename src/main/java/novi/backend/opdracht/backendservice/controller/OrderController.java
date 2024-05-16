package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.OrderRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.OrderOutputDTO;
import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDTO;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.service.OrderService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;
    private final ValidationService validationService;

    public OrderController(OrderService orderService, ValidationService validationService) {
        this.orderService = orderService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequestDTO orderRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = validationService.formatFieldErrors(bindingResult);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        orderService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bestelling succesvol geplaatst.");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            OrderOutputDTO orderDetails = orderService.getOrderDetails(orderId);
            return ResponseEntity.ok(orderDetails);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bestelling niet gevonden");
        } catch (AuthorizationServiceException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Je bent niet gemachtigd om deze bestelling te bekijken");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderOutputDTO>> getUserOrders() {
        List<OrderOutputDTO> userOrders = orderService.getUserOrders();
        return ResponseEntity.ok(userOrders);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Bestelling succesvol geannuleerd");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bestelling niet gevonden");
        } catch (AuthorizationServiceException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Je bent niet gemachtigd om deze bestelling te annuleren");
        }
    }

    @PutMapping("/{orderId}/confirm-shipment")
    public ResponseEntity<String> confirmShipment(@PathVariable Long orderId) {
        ResponseEntity<String> response = orderService.confirmShipment(orderId);
        return response;
    }

    @GetMapping("/{orderId}/receipt")
    public ResponseEntity<?> getReceiptForOrder(@PathVariable Long orderId) {
        try {
            ReceiptOutputDTO receiptOutputDTO = orderService.getReceiptForOrder(orderId);
            return ResponseEntity.ok(receiptOutputDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bon niet gevonden voor bestelling met ID: " + orderId);
        } catch (AuthorizationServiceException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Je bent niet gemachtigd om deze bon te bekijken");
        }
    }
}
