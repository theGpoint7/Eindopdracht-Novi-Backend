package novi.backend.opdracht.backendservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;
import novi.backend.opdracht.backendservice.dto.output.FeedbackOutputDto;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDto;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.service.AuthenticationService;
import novi.backend.opdracht.backendservice.service.FeedbackService;
import novi.backend.opdracht.backendservice.service.ProductService;
import novi.backend.opdracht.backendservice.service.ValidationService;
import novi.backend.opdracht.backendservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private ValidationService validationService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testGetAllProducts_Success() throws Exception {
        ProductOutputDto product = new ProductOutputDto();
        product.setProductId(1L);
        product.setProductName("Test Product");

        FeedbackOutputDto feedback = new FeedbackOutputDto();
        feedback.setFeedbackId(1L);
        feedback.setContent("Great product!");

        product.setFeedbacks(Collections.singletonList(feedback));

        Mockito.when(productService.findAllProducts(any(), any(), any(), any())).thenReturn(Collections.singletonList(product));
        Mockito.when(feedbackService.getFeedbacksByProductId(1L)).thenReturn(Collections.singletonList(feedback));

        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId", is(1)))
                .andExpect(jsonPath("$[0].productName", is("Test Product")))
                .andExpect(jsonPath("$[0].feedbacks[0].content", is("Great product!")));
    }

    @Test
    public void testGetProductById_Success() throws Exception {
        ProductOutputDto product = new ProductOutputDto();
        product.setProductId(1L);
        product.setProductName("Test Product");

        FeedbackOutputDto feedback = new FeedbackOutputDto();
        feedback.setFeedbackId(1L);
        feedback.setContent("Great product!");

        product.setFeedbacks(Collections.singletonList(feedback));

        Mockito.when(productService.getProductById(1L)).thenReturn(product);
        Mockito.when(feedbackService.getFeedbacksByProductId(1L)).thenReturn(Collections.singletonList(feedback));

        mockMvc.perform(get("/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.productName", is("Test Product")))
                .andExpect(jsonPath("$.feedbacks[0].content", is("Great product!")));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(get("/products/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Product not found")))
                .andExpect(jsonPath("$.details", is("Resource not found")));
    }


    @Test
    public void testCreateProduct_Success() throws Exception {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setProductName("New Product");
        productInputDTO.setProductType("Type1");
        productInputDTO.setPrice(10.0);
        productInputDTO.setInventoryCount(100);

        ProductOutputDto productOutputDTO = new ProductOutputDto();
        productOutputDTO.setProductId(1L);
        productOutputDTO.setProductName("New Product");

        ObjectMapper objectMapper = new ObjectMapper();
        String productInputJson = objectMapper.writeValueAsString(productInputDTO);

        Mockito.when(productService.createProduct(any(ProductInputDto.class))).thenReturn(productOutputDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/products/1")))
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.productName", is("New Product")));
    }


    @Test
    public void testCreateProduct_ValidationErrors() throws Exception {
        ProductInputDto productInputDTO = new ProductInputDto();

        ObjectMapper objectMapper = new ObjectMapper();
        String productInputJson = objectMapper.writeValueAsString(productInputDTO);

        Mockito.when(validationService.formatFieldErrors(any(BindingResult.class))).thenReturn("Validation errors occurred");

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productInputJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation errors occurred"));
    }

    @Test
    public void testUpdateProduct_Success() throws Exception {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();
        productUpdateDTO.setProductName("Updated Product");
        productUpdateDTO.setPrice(15.0);
        productUpdateDTO.setInventoryCount(200);

        ProductOutputDto productOutputDTO = new ProductOutputDto();
        productOutputDTO.setProductId(1L);
        productOutputDTO.setProductName("Updated Product");

        ObjectMapper objectMapper = new ObjectMapper();
        String productUpdateJson = objectMapper.writeValueAsString(productUpdateDTO);

        Mockito.when(productService.updateProduct(eq(1L), any(ProductUpdateDto.class))).thenReturn(productOutputDTO);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productUpdateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.productName", is("Updated Product")));
    }


    @Test
    public void testUpdateProduct_ValidationErrors() throws Exception {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();

        ObjectMapper objectMapper = new ObjectMapper();
        String productUpdateJson = objectMapper.writeValueAsString(productUpdateDTO);

        Mockito.when(validationService.formatFieldErrors(any(BindingResult.class))).thenReturn("Validation errors occurred");

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productUpdateJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation errors occurred"));
    }
}
