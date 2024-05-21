package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDto;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Designer designer;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("securepassword");
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEnabledAccount(true);
        user.setAddress("123 Test Street");
        userRepository.save(user);
        designer = new Designer();
        designer.setUser(user);
        designer.setStoreName("Test Store");
        designer.setBio("Test Bio");
        designerRepository.save(designer);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testCreateProduct_Success() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setProductName("New Product");
        productInputDTO.setProductType("Clothing");
        productInputDTO.setPrice(20.0);
        productInputDTO.setInventoryCount(50);
        productInputDTO.setClothingSize("M");
        productInputDTO.setColor("Red");
        productInputDTO.setFit("Regular");

        ProductOutputDto createdProduct = productService.createProduct(productInputDTO);

        assertNotNull(createdProduct);
        assertEquals("New Product", createdProduct.getProductName());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testCreateProduct_ValidationErrors() {
        ProductInputDto productInputDTO = new ProductInputDto();

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productService.createProduct(productInputDTO);
        });

        assertTrue(exception.getMessage().contains("Productnaam mag niet leeg zijn.")
                || exception.getMessage().contains("Producttype mag niet leeg zijn.")
                || exception.getMessage().contains("Prijs moet minimaal 1 zijn."));
    }


    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testCreateProduct_ProductTypeValidationError() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setProductName("Invalid Product");
        productInputDTO.setProductType("InvalidType");
        productInputDTO.setPrice(20.0);
        productInputDTO.setInventoryCount(50);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            productService.createProduct(productInputDTO);
        });

        assertEquals("Ongeldig producttype: InvalidType", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testGetProductById_Success() {
        Clothing product = new Clothing();
        product.setProductName("Test Product");
        product.setProductType("Clothing");
        product.setPrice(50.0);
        product.setInventoryCount(10);
        product.setDesigner(designer);
        product.setClothingSize("L");
        product.setColor("Blue");
        product.setFit("Slim");
        productRepository.save(product);

        ProductOutputDto foundProduct = productService.getProductById(product.getProductId());

        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getProductName());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testGetProductById_NotFound() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(9999L);
        });

        assertEquals("Product niet gevonden", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testUpdateProduct_Success() {
        Clothing product = new Clothing();
        product.setProductName("Test Product");
        product.setProductType("Clothing");
        product.setPrice(50.0);
        product.setInventoryCount(10);
        product.setDesigner(designer);
        product.setClothingSize("L");
        product.setColor("Blue");
        product.setFit("Slim");
        productRepository.save(product);

        ProductUpdateDto updateDTO = new ProductUpdateDto();
        updateDTO.setProductName("Updated Product");
        updateDTO.setPrice(60.0);
        updateDTO.setInventoryCount(20);
        updateDTO.setClothingSize("XL");
        updateDTO.setColor("Green");
        updateDTO.setFit("Loose");

        ProductOutputDto updatedProduct = productService.updateProduct(product.getProductId(), updateDTO);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getProductName());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "DESIGNER")
    public void testUpdateProduct_NotFound() {
        ProductUpdateDto updateDTO = new ProductUpdateDto();
        updateDTO.setProductName("Updated Product");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(9999L, updateDTO);
        });

        assertEquals("Product niet gevonden", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    public void testFindAllProducts() {
        Clothing clothing1 = new Clothing();
        clothing1.setProductName("Product 1");
        clothing1.setProductType("Clothing");
        clothing1.setPrice(30.0);
        clothing1.setInventoryCount(5);
        clothing1.setDesigner(designer);
        clothing1.setClothingSize("S");
        clothing1.setColor("Red");
        clothing1.setFit("Slim");
        productRepository.save(clothing1);

        Clothing clothing2 = new Clothing();
        clothing2.setProductName("Product 2");
        clothing2.setProductType("Clothing");
        clothing2.setPrice(40.0);
        clothing2.setInventoryCount(15);
        clothing2.setDesigner(designer);
        clothing2.setClothingSize("M");
        clothing2.setColor("Blue");
        clothing2.setFit("Regular");
        productRepository.save(clothing2);
        List<ProductOutputDto> allProducts = productService.findAllProducts(null, null, null, null);
        assertEquals(3, allProducts.size());
        List<ProductOutputDto> sizeMProducts = productService.findAllProducts(null, "M", null, null);
        assertEquals(1, sizeMProducts.size());
        assertEquals("Product 2", sizeMProducts.get(0).getProductName());
        List<ProductOutputDto> blueProducts = productService.findAllProducts(null, null, "Blue", null);
        assertEquals(1, blueProducts.size());
        assertEquals("Product 2", blueProducts.get(0).getProductName());
        List<ProductOutputDto> storeProducts = productService.findAllProducts(null, null, null, "Test Store");
        assertEquals(2, storeProducts.size());
    }
}
