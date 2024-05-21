package novi.backend.opdracht.backendservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import novi.backend.opdracht.backendservice.dto.input.CartItemInputDto;
import novi.backend.opdracht.backendservice.dto.input.OrderRequestDto;
import novi.backend.opdracht.backendservice.dto.output.OrderOutputDto;
import novi.backend.opdracht.backendservice.dto.output.OrderLineOutputDto;
import novi.backend.opdracht.backendservice.dto.output.ReceiptOutputDto;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.service.AuthenticationService;
import novi.backend.opdracht.backendservice.service.OrderService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import novi.backend.opdracht.backendservice.model.OrderStatus;
import novi.backend.opdracht.backendservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ValidationService validationService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetOrderDetails_Success() throws Exception {
        OrderLineOutputDto orderLine = new OrderLineOutputDto();
        orderLine.setOrderLineId(1L);
        orderLine.setProductId(1L);
        orderLine.setProductName("Product Name");
        orderLine.setPrice(100.0);
        orderLine.setQuantity(1);

        OrderOutputDto orderOutput = new OrderOutputDto();
        orderOutput.setOrderId(1L);
        orderOutput.setUsername("testuser");
        orderOutput.setOrderLines(Collections.singletonList(orderLine));
        orderOutput.setPaymentMethodType("Credit Card");
        orderOutput.setAmount(100.0);
        orderOutput.setOrderDateTime(LocalDateTime.now());
        orderOutput.setShippingAddress("123 Test Street");
        orderOutput.setOrderStatus(OrderStatus.PENDING);
        orderOutput.setPaymentStatus("Paid");

        Mockito.when(orderService.getOrderDetails(1L)).thenReturn(orderOutput);

        mockMvc.perform(get("/orders/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.orderLines[0].productName", is("Product Name")))
                .andExpect(jsonPath("$.orderLines[0].price", is(100.0)))
                .andExpect(jsonPath("$.orderLines[0].quantity", is(1)))
                .andExpect(jsonPath("$.paymentMethodType", is("Credit Card")))
                .andExpect(jsonPath("$.amount", is(100.0)))
                .andExpect(jsonPath("$.shippingAddress", is("123 Test Street")))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.PENDING.name())))
                .andExpect(jsonPath("$.paymentStatus", is("Paid")))
                .andExpect(jsonPath("$.orderDateTime", startsWith(orderOutput.getOrderDateTime().toString().substring(0, 19))));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetOrderDetails_NotFound() throws Exception {
        Mockito.when(orderService.getOrderDetails(1L)).thenThrow(new ResourceNotFoundException("Bestelling niet gevonden"));

        mockMvc.perform(get("/orders/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bestelling niet gevonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetOrderDetails_Forbidden() throws Exception {
        Mockito.when(orderService.getOrderDetails(1L)).thenThrow(new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te bekijken"));

        mockMvc.perform(get("/orders/1"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("Je bent niet gemachtigd om deze bestelling te bekijken"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testPlaceOrder_Success() throws Exception {
        CartItemInputDto cartItemInputDTO = new CartItemInputDto();
        cartItemInputDTO.setProductId(1L);
        cartItemInputDTO.setQuantity(2);

        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(true);
        orderRequestDTO.setCartItems(Collections.singletonList(cartItemInputDTO));

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequestDTO);

        Mockito.doNothing().when(orderService).placeOrder(any(OrderRequestDto.class));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Bestelling succesvol geplaatst."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testPlaceOrder_ValidationErrors() throws Exception {
        OrderRequestDto orderRequestDTO = new OrderRequestDto();
        orderRequestDTO.setRetrieveCartItems(false);
        orderRequestDTO.setCartItems(Collections.emptyList());

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestJson = objectMapper.writeValueAsString(orderRequestDTO);

        Mockito.when(validationService.formatFieldErrors(any(BindingResult.class))).thenReturn("Validation errors occurred");

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation errors occurred"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetUserOrders_Success() throws Exception {
        OrderLineOutputDto orderLine = new OrderLineOutputDto();
        orderLine.setOrderLineId(1L);
        orderLine.setProductId(1L);
        orderLine.setProductName("Product Name");
        orderLine.setPrice(100.0);
        orderLine.setQuantity(1);

        OrderOutputDto orderOutput = new OrderOutputDto();
        orderOutput.setOrderId(1L);
        orderOutput.setUsername("testuser");
        orderOutput.setOrderLines(Collections.singletonList(orderLine));
        orderOutput.setPaymentMethodType("Credit Card");
        orderOutput.setAmount(100.0);
        orderOutput.setOrderDateTime(LocalDateTime.now());
        orderOutput.setShippingAddress("123 Test Street");
        orderOutput.setOrderStatus(OrderStatus.PENDING);
        orderOutput.setPaymentStatus("Paid");

        Mockito.when(orderService.getUserOrders()).thenReturn(Collections.singletonList(orderOutput));

        mockMvc.perform(get("/orders/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[0].orderLines[0].productName", is("Product Name")))
                .andExpect(jsonPath("$[0].orderLines[0].price", is(100.0)))
                .andExpect(jsonPath("$[0].orderLines[0].quantity", is(1)))
                .andExpect(jsonPath("$[0].paymentMethodType", is("Credit Card")))
                .andExpect(jsonPath("$[0].amount", is(100.0)))
                .andExpect(jsonPath("$[0].shippingAddress", is("123 Test Street")))
                .andExpect(jsonPath("$[0].orderStatus", is(OrderStatus.PENDING.name())))
                .andExpect(jsonPath("$[0].paymentStatus", is("Paid")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testCancelOrder_Success() throws Exception {
        Mockito.doNothing().when(orderService).cancelOrder(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/cancel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Bestelling succesvol geannuleerd"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testCancelOrder_NotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Bestelling niet gevonden")).when(orderService).cancelOrder(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/cancel"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bestelling niet gevonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testCancelOrder_Forbidden() throws Exception {
        Mockito.doThrow(new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te annuleren")).when(orderService).cancelOrder(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/cancel"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("Je bent niet gemachtigd om deze bestelling te annuleren"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_Success() throws Exception {
        Mockito.when(orderService.confirmShipment(1L)).thenReturn(new ResponseEntity<>("Bestelling succesvol verzonden", HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/confirm-shipment"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Bestelling succesvol verzonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_NotFound() throws Exception {
        Mockito.when(orderService.confirmShipment(1L)).thenThrow(new ResourceNotFoundException("Bestelling niet gevonden"));

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/confirm-shipment"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bestelling niet gevonden"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_Forbidden() throws Exception {
        Mockito.when(orderService.confirmShipment(1L)).thenThrow(new AuthorizationServiceException("Je bent niet gemachtigd om deze bestelling te bevestigen"));

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/confirm-shipment"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("Je bent niet gemachtigd om deze bestelling te bevestigen"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_Conflict() throws Exception {
        Mockito.when(orderService.confirmShipment(1L)).thenThrow(new IllegalStateException("Bestelling is al verzonden of betaling is niet bevestigd"));

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/confirm-shipment"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string("Bestelling is al verzonden of betaling is niet bevestigd"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testConfirmShipment_InternalServerError() throws Exception {
        Mockito.when(orderService.confirmShipment(1L)).thenThrow(new RuntimeException("Er is een fout opgetreden bij het bevestigen van de verzending."));

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1/confirm-shipment"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Er is een fout opgetreden bij het bevestigen van de verzending."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetReceiptForOrder_Success() throws Exception {
        ReceiptOutputDto receiptOutput = new ReceiptOutputDto();
        Mockito.when(orderService.getReceiptForOrder(1L)).thenReturn(receiptOutput);

        mockMvc.perform(get("/orders/1/receipt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetReceiptForOrder_NotFound() throws Exception {
        Mockito.when(orderService.getReceiptForOrder(1L)).thenThrow(new ResourceNotFoundException("Bon niet gevonden voor bestelling met ID: 1"));

        mockMvc.perform(get("/orders/1/receipt"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bon niet gevonden voor bestelling met ID: 1"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testGetReceiptForOrder_Forbidden() throws Exception {
        Mockito.when(orderService.getReceiptForOrder(1L)).thenThrow(new AuthorizationServiceException("Je bent niet gemachtigd om deze bon te bekijken"));

        mockMvc.perform(get("/orders/1/receipt"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("Je bent niet gemachtigd om deze bon te bekijken"));
    }
}
