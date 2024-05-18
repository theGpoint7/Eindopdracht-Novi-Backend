package novi.backend.opdracht.backendservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDTO;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDTO;
import novi.backend.opdracht.backendservice.model.Clothing;
import novi.backend.opdracht.backendservice.model.Designer;
import novi.backend.opdracht.backendservice.model.User;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import novi.backend.opdracht.backendservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Designer designer;
    private Clothing clothing;

    @BeforeEach
    void setUp() throws Exception {
        String userJson = """
        {
            "username": "testuser",
            "password": "securepassword",
            "enabled": true,
            "email": "testuser@example.com",
            "firstName": "Test",
            "lastName": "User",
            "dateOfBirth": "1981-11-11",
            "address": "123 Test Street",
            "phoneNo": "084-23432331"
        }
        """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        user = userRepository.findByUsername("testuser").orElseThrow();

        designer = new Designer();
        designer.setUser(user);
        designer.setStoreName("Test Store");
        designer.setBio("Test Bio");
        designerRepository.save(designer);

        clothing = new Clothing();
        clothing.setProductName("Test Product");
        clothing.setProductType("Clothing");
        clothing.setPrice(10.0);
        clothing.setInventoryCount(100);
        clothing.setDesigner(designer);
        clothing.setClothingSize("M");
        clothing.setColor("Red");
        clothing.setFit("Regular");
        productRepository.save(clothing);
    }

    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    @Test
    void testGetAllProducts_Success() throws Exception {
        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId", is(clothing.getProductId().intValue())))
                .andExpect(jsonPath("$[0].productName", is("Test Product")));
    }

    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    @Test
    void testGetProductById_Success() throws Exception {
        mockMvc.perform(get("/products/" + clothing.getProductId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId", is(clothing.getProductId().intValue())))
                .andExpect(jsonPath("$.productName", is("Test Product")));
    }

    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    @Test
    void testGetProductById_NotFound() throws Exception {
        mockMvc.perform(get("/products/9999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Product niet gevonden")));
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testCreateProduct_Success() throws Exception {
        String productInputJson = """
        {
            "productName": "Nieuw Product",
            "productType": "Clothing",
            "price": 10.0,
            "inventoryCount": 100,
            "clothingSize": "L",
            "color": "Blauw",
            "fit": "Slim"
        }
        """;

        MvcResult result = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/products/")))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ProductOutputDTO createdProduct = objectMapper.readValue(responseJson, ProductOutputDTO.class);

        assertEquals("New Product", createdProduct.getProductName());
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testCreateProduct_ValidationErrors() throws Exception {
        ProductInputDTO productInputDTO = new ProductInputDTO();
        ObjectMapper objectMapper = new ObjectMapper();
        String productInputJson = objectMapper.writeValueAsString(productInputDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productInputJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Prijs moet minimaal 1 zijn.")))
                .andExpect(content().string(containsString("Producttype mag niet leeg zijn.")))
                .andExpect(content().string(containsString("Productnaam mag niet leeg zijn.")));
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testUpdateProduct_Success() throws Exception {
        String productUpdateJson = """
        {
            "productName": "Updated Product",
            "price": 15.0,
            "inventoryCount": 200,
            "clothingSize": "XL",
            "color": "Groen",
            "fit": "tight"
        }
        """;

        mockMvc.perform(put("/products/" + clothing.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productUpdateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(clothing.getProductId().intValue())))
                .andExpect(jsonPath("$.productName", is("Updated Product")));
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testUpdateProduct_ValidationErrors() throws Exception {
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();
        ObjectMapper objectMapper = new ObjectMapper();
        String productUpdateJson = objectMapper.writeValueAsString(productUpdateDTO);

        mockMvc.perform(put("/products/" + clothing.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productUpdateJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Prijs moet minimaal 1 zijn.")))
                .andExpect(content().string(containsString("Productnaam mag niet leeg zijn.")));
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testUpdateNonExistentProduct() throws Exception {
        String productUpdateJson = """
        {
            "productName": "Updated Product",
            "price": 15.0,
            "inventoryCount": 200,
            "clothingSize": "XL",
            "color": "Green",
            "fit": "Loose"
        }
        """;

        mockMvc.perform(put("/products/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productUpdateJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Product niet gevonden")));
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testCreateProduct_LongName() throws Exception {
        String productInputJson = """
        {
            "productName": "This is a very long product name that exceeds the normal limit of 255 characters, so it should fail the validation check if the system is working correctly. This is a very long product name that exceeds the normal limit of 255 characters, so it should fail the validation check if the system is working correctly.",
            "productType": "Clothing",
            "price": 10.0,
            "inventoryCount": 100
        }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productInputJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Productnaam mag maximaal 255 karakters bevatten.")));
    }

    @WithMockUser(username = "testuser", roles = "DESIGNER")
    @Test
    void testCreateProduct_HighPrice() throws Exception {
        String productInputJson = """
        {
            "productName": "duur Product",
            "productType": "Clothing",
            "price": 1000000.0,
            "inventoryCount": 100
        }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productInputJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", is("Expensive Product")))
                .andExpect(jsonPath("$.price", is(1000000.0)));
    }
}
