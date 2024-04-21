//package novi.backend.opdracht.backendservice.dto.inputdtos;
//
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import novi.backend.opdracht.backendservice.model.PaymentStatus;
//
//import java.util.List;
//
//public class OrderInputDto {
//    public Long id;
//    public String user;
//    @NotBlank
//    public String deliveryAddress;
//    public List<OrderLineInputDto> orderLines;
//    @Min(1)
//    public double subtotal;
//    @Min(1)
//    public double shippingCosts;
//    @Min(1)
//    public double total;
//    @NotBlank
//    public String paymentMethod;
//    public PaymentStatus paymentStatus;
//}
