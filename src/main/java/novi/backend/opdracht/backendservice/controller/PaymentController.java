package novi.backend.opdracht.backendservice.controller;

import jakarta.validation.Valid;
import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDto;
import novi.backend.opdracht.backendservice.dto.output.PaymentOutputDto;
import novi.backend.opdracht.backendservice.service.PaymentService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ValidationService validationService;

    public PaymentController(PaymentService paymentService, ValidationService validationService) {
        this.paymentService = paymentService;
        this.validationService = validationService;
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<?> confirmPayment(@PathVariable Long orderId, @Valid @RequestBody PaymentConfirmationRequestDto requestDTO, BindingResult result) {
        if (result.hasFieldErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        boolean paymentConfirmed = paymentService.confirmPayment(orderId, requestDTO);
        if (paymentConfirmed) {
            return ResponseEntity.ok("Betaling succesvol bevestigd.");
        } else {
            return ResponseEntity.badRequest().body("Betaling bevestiging mislukt.");
        }
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<?> processPayment(@PathVariable Long orderId, @Valid @RequestBody PaymentConfirmationRequestDto requestDTO, BindingResult result) {
        if (result.hasFieldErrors()) {
            String errorMessage = validationService.formatFieldErrors(result);
            return ResponseEntity.badRequest().body(errorMessage);
        }
        PaymentOutputDto paymentOutputDTO = paymentService.processPaymentAfterOrderPlacement(orderId, requestDTO);
        return ResponseEntity.ok(paymentOutputDTO);
    }
}
