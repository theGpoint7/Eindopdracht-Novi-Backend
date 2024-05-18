package novi.backend.opdracht.backendservice.service;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDTO;
import novi.backend.opdracht.backendservice.dto.output.ProductOutputDTO;
import novi.backend.opdracht.backendservice.exception.BadRequestException;
import novi.backend.opdracht.backendservice.exception.ProductNameTooLongException;
import novi.backend.opdracht.backendservice.exception.ResourceNotFoundException;
import novi.backend.opdracht.backendservice.model.*;
import novi.backend.opdracht.backendservice.repository.DesignerRepository;
import novi.backend.opdracht.backendservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DesignerRepository designerRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private ProductService productService;

    private User user;
    private Designer designer;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");

        designer = new Designer();
        designer.setDesignerId(1L);
        designer.setUser(user);
        designer.setStoreName("Test Store");

        lenient().when(authenticationService.getCurrentUser()).thenReturn(user);
        lenient().when(designerRepository.findByUserUsername(user.getUsername())).thenReturn(Optional.of(designer));
    }

    @Test
    void testGetProductById_Success() {
        AbstractProduct product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setPrice(100.0);
        product.setDesigner(designer);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductOutputDTO productOutputDTO = productService.getProductById(1L);

        assertNotNull(productOutputDTO);
        assertEquals(1L, productOutputDTO.getProductId());
        assertEquals("Test Product", productOutputDTO.getProductName());
    }

    @Test
    void testGetProductById_NotFound() {
        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
        assertEquals("Product niet gevonden", exception.getMessage());
    }

    @Test
    void testCreateProduct_Success() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setProductName("New Product");
        inputDTO.setProductType("Clothing");
        inputDTO.setPrice(100.0);
        inputDTO.setInventoryCount(10);

        AbstractProduct product = new Clothing();
        product.setProductName(inputDTO.getProductName());
        product.setDesigner(designer);
        product.setPrice(inputDTO.getPrice());

        Mockito.when(productRepository.existsByProductName(inputDTO.getProductName())).thenReturn(false);
        Mockito.when(productRepository.save(any(AbstractProduct.class))).thenReturn(product);

        ProductOutputDTO productOutputDTO = productService.createProduct(inputDTO);

        assertNotNull(productOutputDTO);
        assertEquals("New Product", productOutputDTO.getProductName());
    }

    @Test
    void testCreateProduct_DuplicateName() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setProductName("Existing Product");
        inputDTO.setProductType("Clothing");

        Mockito.when(productRepository.existsByProductName(inputDTO.getProductName())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.createProduct(inputDTO));
        assertEquals("U kunt dit product niet maken, de naam bestaat al.", exception.getMessage());
    }

    @Test
    void testCreateProduct_InvalidType() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setProductName("Invalid Product");
        inputDTO.setProductType("InvalidType");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.createProduct(inputDTO));
        assertEquals("Ongeldig producttype: InvalidType", exception.getMessage());
    }

    @Test
    void testCreateProduct_Accessory() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setProductName("New Accessory");
        inputDTO.setProductType("Accessory");
        inputDTO.setPrice(50.0);
        inputDTO.setInventoryCount(20);
        inputDTO.setAccessoryType("BAG");  // Set a valid AccessoryType value

        AbstractProduct product = new Accessory();
        product.setProductName(inputDTO.getProductName());
        product.setDesigner(designer);
        product.setPrice(inputDTO.getPrice());
        ((Accessory) product).setAccessoryType(AccessoryType.valueOf(inputDTO.getAccessoryType()));

        Mockito.when(productRepository.existsByProductName(inputDTO.getProductName())).thenReturn(false);
        Mockito.when(productRepository.save(any(AbstractProduct.class))).thenReturn(product);

        ProductOutputDTO productOutputDTO = productService.createProduct(inputDTO);

        assertNotNull(productOutputDTO);
        assertEquals("New Accessory", productOutputDTO.getProductName());
    }

    @Test
    void testCreateProduct_Footwear() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        inputDTO.setProductName("New Footwear");
        inputDTO.setProductType("Footwear");
        inputDTO.setPrice(150.0);
        inputDTO.setInventoryCount(30);

        AbstractProduct product = new Footwear();
        product.setProductName(inputDTO.getProductName());
        product.setDesigner(designer);
        product.setPrice(inputDTO.getPrice());

        Mockito.when(productRepository.existsByProductName(inputDTO.getProductName())).thenReturn(false);
        Mockito.when(productRepository.save(any(AbstractProduct.class))).thenReturn(product);

        ProductOutputDTO productOutputDTO = productService.createProduct(inputDTO);

        assertNotNull(productOutputDTO);
        assertEquals("New Footwear", productOutputDTO.getProductName());
    }

    @Test
    void testUpdateProduct_Success() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setProductName("Updated Product");

        AbstractProduct product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Old Product");
        product.setDesigner(designer);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(any(AbstractProduct.class))).thenReturn(product);

        ProductOutputDTO productOutputDTO = productService.updateProduct(1L, updateDTO);

        assertNotNull(productOutputDTO);
        assertEquals("Updated Product", productOutputDTO.getProductName());
    }

    @Test
    void testUpdateProduct_NotFound() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setProductName("Updated Product");

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, updateDTO));
        assertEquals("Product niet gevonden", exception.getMessage());
    }

    @Test
    void testUpdateProduct_DuplicateName() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setProductName("Existing Product");

        AbstractProduct product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Old Product");
        product.setDesigner(designer);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.existsByProductName(updateDTO.getProductName())).thenReturn(true);

        ProductNameTooLongException exception = assertThrows(ProductNameTooLongException.class, () -> productService.updateProduct(1L, updateDTO));
        assertEquals("Productnaam bestaat al", exception.getMessage());
    }

    @Test
    void testUpdateProduct_Unauthorized() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setProductName("Updated Product");

        Designer anotherDesigner = new Designer();
        anotherDesigner.setDesignerId(2L);
        anotherDesigner.setUser(new User());

        AbstractProduct product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Old Product");
        product.setDesigner(anotherDesigner);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> productService.updateProduct(1L, updateDTO));
        assertEquals("U bent niet gemachtigd om dit product bij te werken", exception.getMessage());
    }

    @Test
    void testUpdateProduct_UpdateSpecificFields() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setProductName("Updated Product");
        updateDTO.setAccessoryType("BAG");  // Set a valid AccessoryType value

        AbstractProduct product = new Accessory();
        product.setProductId(1L);
        product.setProductName("Old Product");
        product.setDesigner(designer);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(any(AbstractProduct.class))).thenReturn(product);

        ProductOutputDTO productOutputDTO = productService.updateProduct(1L, updateDTO);

        assertNotNull(productOutputDTO);
        assertEquals("Updated Product", productOutputDTO.getProductName());
    }

    @Test
    void testUpdateProduct_Footwear() {
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setProductName("Updated Footwear");
        updateDTO.setFootwearSize(42);
        updateDTO.setGender("Male");

        AbstractProduct product = new Footwear();
        product.setProductId(1L);
        product.setProductName("Old Footwear");
        product.setDesigner(designer);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(any(AbstractProduct.class))).thenReturn(product);

        ProductOutputDTO productOutputDTO = productService.updateProduct(1L, updateDTO);

        assertNotNull(productOutputDTO);
        assertEquals("Updated Footwear", productOutputDTO.getProductName());
    }

    @Test
    void testFindAllProducts_FilterByStoreName() {
        AbstractProduct product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Product 1");
        product.setDesigner(designer);

        Mockito.when(productRepository.findByDesignerStoreName("Test Store")).thenReturn(List.of(product));

        List<ProductOutputDTO> products = productService.findAllProducts(null, null, null, "Test Store");

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getProductName());
    }

    @Test
    void testFindAllProducts_FilterByFootwearSize() {
        Footwear product = new Footwear();
        product.setProductId(1L);
        product.setProductName("Footwear 1");
        product.setDesigner(designer);
        product.setFootwearSize(42);

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductOutputDTO> products = productService.findAllProducts(42, null, null, null);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Footwear 1", products.get(0).getProductName());
    }

    @Test
    void testFindAllProducts_FilterByClothingSize() {
        Clothing product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Clothing 1");
        product.setDesigner(designer);
        product.setClothingSize("M");

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductOutputDTO> products = productService.findAllProducts(null, "M", null, null);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Clothing 1", products.get(0).getProductName());
    }

    @Test
    void testFindAllProducts_FilterByColor() {
        Clothing product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Clothing 1");
        product.setDesigner(designer);
        product.setColor("Red");

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductOutputDTO> products = productService.findAllProducts(null, null, "Red", null);

        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Clothing 1", products.get(0).getProductName());
    }

    @Test
    void testToProductOutputDTO_WithPromotion() {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(1L);
        promotion.setPromotionDescription("Spring Sale");
        promotion.setPromotionPercentage(20.0);
        promotion.setPromotionEndDateTime(LocalDateTime.now().plusDays(7));

        AbstractProduct product = new Clothing();
        product.setProductId(1L);
        product.setProductName("Product 1");
        product.setDesigner(designer);
        product.setPromotion(promotion);

        ProductOutputDTO dto = productService.toProductOutputDTO(product);

        assertNotNull(dto);
        assertNotNull(dto.getPromotion());
        assertEquals(1L, dto.getPromotion().getPromotionId());
        assertEquals("Spring Sale", dto.getPromotion().getPromotionDetails());
    }

    @Test
    void testToProductOutputDTO_WithPromotion_Alternative() {
        Promotion promotion = new Promotion();
        promotion.setPromotionId(2L);
        promotion.setPromotionDescription("Summer Sale");
        promotion.setPromotionPercentage(15.0);
        promotion.setPromotionEndDateTime(LocalDateTime.now().plusDays(10));

        AbstractProduct product = new Footwear();
        product.setProductId(2L);
        product.setProductName("Product 2");
        product.setDesigner(designer);
        product.setPromotion(promotion);

        ProductOutputDTO dto = productService.toProductOutputDTO(product);

        assertNotNull(dto);
        assertNotNull(dto.getPromotion());
        assertEquals(2L, dto.getPromotion().getPromotionId());
        assertEquals("Summer Sale", dto.getPromotion().getPromotionDetails());
    }
}
