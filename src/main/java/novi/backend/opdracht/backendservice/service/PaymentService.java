package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.PaymentConfirmationRequestDTO;
import novi.backend.opdracht.backendservice.dto.output.PaymentOutputDTO;
import novi.backend.opdracht.backendservice.exception.AccessDeniedException;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.OrderRepository;
import novi.backend.opdracht.backendservice.repository.PaymentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentOutputDTO toPaymentOutputDTO(AbstractPaymentMethod paymentMethod) {
        PaymentOutputDTO dto = new PaymentOutputDTO();
        dto.setPaymentId(paymentMethod.getPaymentMethodId());
        dto.setPaymentMethodType(paymentMethod.getPaymentMethodType());
        if (paymentMethod instanceof BankTransferPayment bankTransferPayment) {
            dto.setAccountNumber(bankTransferPayment.getAccountNumber());
            dto.setBankCode(bankTransferPayment.getBankCode());
        } else if (paymentMethod instanceof PayPalPayment payPalPayment) {
            dto.setEmail(payPalPayment.getEmail());
            dto.setPassword(payPalPayment.getPassword());
        } else if (paymentMethod instanceof CreditCardPayment creditCardPayment) {
            dto.setCardNumber(creditCardPayment.getCardNumber());
            dto.setExpiryDate(creditCardPayment.getExpiryDate());
            dto.setCvv(creditCardPayment.getCvv());
        }
        return dto;
    }

    public boolean confirmPayment(Long orderId, PaymentConfirmationRequestDTO requestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order niet gevonden"));
        if (order.getPaymentStatus() != PaymentStatus.PROCESSING) {
            throw new AccessDeniedException("Betaling kan niet worden bevestigd omdat deze niet wordt verwerkt.");
        }
        if (order.getPaymentStatus() == PaymentStatus.FAILED) {
            throw new AccessDeniedException("Betaling is al mislukt, plaats een nieuwe bestelling.");
        }

        AbstractPaymentMethod paymentMethod = order.getPaymentMethod();
        boolean paymentMatches = paymentMethod.matchesPaymentDetails(requestDTO);

        if (paymentMatches) {
            order.setPaymentStatus(PaymentStatus.CONFIRMED);
            order.setOrderStatus(OrderStatus.WAITING_FOR_SHIPMENT);
            paymentMethod.setPaymentCompletionDateTime(LocalDateTime.now());
        } else {
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setOrderStatus(OrderStatus.CANCELED);
            for (OrderLine orderLine : order.getOrderLines()) {
                AbstractProduct product = orderLine.getProduct();
                product.setInventoryCount(product.getInventoryCount() + orderLine.getQuantity());
            }
        }
        orderRepository.save(order);
        return paymentMatches;
    }

    public PaymentOutputDTO processPaymentAfterOrderPlacement(Long orderId, PaymentConfirmationRequestDTO requestDTO) {
        if (!orderCorrespondsToUser(orderId)) {
            throw new BadRequestException("Order komt niet overeen met de gebruiker.");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order niet gevonden"));
        AbstractPaymentMethod paymentMethod;
        switch (requestDTO.getPaymentMethodType()) {
            case "BANK_TRANSFER":
                BankTransferPayment bankTransferPayment = new BankTransferPayment();
                bankTransferPayment.setAccountNumber(requestDTO.getAccountNumber());
                bankTransferPayment.setBankCode(requestDTO.getBankCode());
                paymentMethod = bankTransferPayment;
                break;
            case "PAYPAL":
                PayPalPayment payPalPayment = new PayPalPayment();
                payPalPayment.setEmail(requestDTO.getEmail());
                payPalPayment.setPassword(requestDTO.getPassword());
                paymentMethod = payPalPayment;
                break;
            case "CREDIT_CARD":
                CreditCardPayment creditCardPayment = new CreditCardPayment();
                creditCardPayment.setCardNumber(requestDTO.getCardNumber());
                creditCardPayment.setExpiryDate(requestDTO.getExpiryDate());
                creditCardPayment.setCvv(requestDTO.getCvv());
                paymentMethod = creditCardPayment;
                break;
            default:
                throw new BadRequestException("Ongeldig type betalingsmethode.");
        }
        paymentMethod.setOrder(order);

        LocalDateTime paymentCreationDateTime = LocalDateTime.now();
        paymentMethod.setPaymentCreationDateTime(paymentCreationDateTime);
        LocalDateTime paymentMethodExpirationDateTime = paymentCreationDateTime.plusHours(24);
        paymentMethod.setPaymentMethodExpirationDateTime(paymentMethodExpirationDateTime);
        paymentMethod = paymentRepository.save(paymentMethod);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(PaymentStatus.PROCESSING);
        orderRepository.save(order);
        return toPaymentOutputDTO(paymentMethod);
    }

    private boolean orderCorrespondsToUser(Long orderId) {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepository.findById(orderId)
                .map(order -> order.getUser().getUsername().equals(authenticatedUsername))
                .orElse(false);
    }
}
