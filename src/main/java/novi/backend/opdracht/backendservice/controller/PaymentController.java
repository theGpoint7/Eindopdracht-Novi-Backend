package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.PaymentOutputDTO;
import novi.backend.opdracht.backendservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<String> confirmPayment(@PathVariable Long orderId, @Valid @RequestBody PaymentConfirmationRequestDTO requestDTO) {
        boolean paymentConfirmed = paymentService.confirmPayment(orderId, requestDTO);
        if (paymentConfirmed) {
            return ResponseEntity.ok("Betaling succesvol bevestigd.");
        } else {
            return ResponseEntity.badRequest().body("Betaling bevestiging mislukt.");
        }
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<PaymentOutputDTO> processPayment(@PathVariable Long orderId, @Valid @RequestBody PaymentConfirmationRequestDTO requestDTO) {
        PaymentOutputDTO paymentOutputDTO = paymentService.processPaymentAfterOrderPlacement(orderId, requestDTO);
        return ResponseEntity.ok(paymentOutputDTO);
    }
}
