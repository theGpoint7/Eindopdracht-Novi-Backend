package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractProductTest {

    private AbstractProduct product;

    @BeforeEach
    public void setUp() {
        product = new TestProduct();
    }

    @Test
    public void testSetAndGetProductId() {
        Long expectedProductId = 1L;
        product.setProductId(expectedProductId);
        Long actualProductId = product.getProductId();
        assertEquals(expectedProductId, actualProductId);
    }

    @Test
    public void testSetAndGetProductName() {
        String expectedProductName = "TestProduct";
        product.setProductName(expectedProductName);
        String actualProductName = product.getProductName();
        assertEquals(expectedProductName, actualProductName);
    }

    @Test
    public void testSetAndGetProductType() {
        String expectedProductType = "footwear";
        product.setProductType(expectedProductType);
        String actualProductType = product.getProductType();
        assertEquals(expectedProductType, actualProductType);
    }

    @Test
    public void testSetAndGetPrice() {
        double expectedPrice = 100.0;
        product.setPrice(expectedPrice);
        double actualPrice = product.getPrice();
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    public void testSetAndGetInventoryCount() {
        int expectedInventoryCount = 10;
        product.setInventoryCount(expectedInventoryCount);
        int actualInventoryCount = product.getInventoryCount();
        assertEquals(expectedInventoryCount, actualInventoryCount);
    }

    @Test
    public void testSetAndGetImageUrl() {
        String expectedImageUrl = "http://example.com/image.jpg";
        product.setImageUrl(expectedImageUrl);
        String actualImageUrl = product.getImageUrl();
        assertEquals(expectedImageUrl, actualImageUrl);
    }

    @Test
    public void testSetAndGetProductDescription() {
        String expectedProductDescription = "Test product.";
        product.setProductDescription(expectedProductDescription);
        String actualProductDescription = product.getProductDescription();
        assertEquals(expectedProductDescription, actualProductDescription);
    }

    @Test
    public void testSetAndGetMaterial() {
        String expectedMaterial = "Cotton";
        product.setMaterial(expectedMaterial);
        String actualMaterial = product.getMaterial();
        assertEquals(expectedMaterial, actualMaterial);
    }

    @Test
    public void testSetAndGetDesigner() {
        Designer expectedDesigner = new Designer();
        product.setDesigner(expectedDesigner);
        Designer actualDesigner = product.getDesigner();
        assertEquals(expectedDesigner, actualDesigner);
    }

    @Test
    public void testSetAndGetPromotion() {
        Promotion expectedPromotion = new Promotion();
        product.setPromotion(expectedPromotion);
        Promotion actualPromotion = product.getPromotion();
        assertEquals(expectedPromotion, actualPromotion);
    }

    @Test
    public void testInsertCommonFields() {
        ProductInputDTO productInputDTO = new ProductInputDTO();
        productInputDTO.setProductName("testproduct-naam");
        productInputDTO.setProductType("footwear");
        productInputDTO.setPrice(200.0);
        productInputDTO.setInventoryCount(20);
        productInputDTO.setImageUrl("http://voorbeeld.nl/afbeeldingzijdeschoenen.jpg");
        productInputDTO.setProductDescription("testomschrijving.");
        productInputDTO.setMaterial("zijde");

        product.insertCommonFields(productInputDTO);

        assertEquals("testproduct-naam", product.getProductName());
        assertEquals("footwear", product.getProductType());
        assertEquals(200.0, product.getPrice());
        assertEquals(20, product.getInventoryCount());
        assertEquals("http://voorbeeld.nl/afbeeldingzijdeschoenen.jpg", product.getImageUrl());
        assertEquals("testomschrijving.", product.getProductDescription());
        assertEquals("zijde", product.getMaterial());
    }

    @Test
    public void testUpdateCommonFields() {
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();
        productUpdateDTO.setProductName("Testproductnaam-geupdate");
        productUpdateDTO.setPrice(200.0);
        productUpdateDTO.setInventoryCount(20);
        productUpdateDTO.setImageUrl("http://voorbeeld.nl/geupdateafbeeldinglerenschoenen.jpg");
        productUpdateDTO.setProductDescription("testomschrijving-geupdate");
        productUpdateDTO.setMaterial("Leer");

        product.updateCommonFields(productUpdateDTO);

        assertEquals("Testproductnaam-geupdate", product.getProductName());
        assertEquals(200.0, product.getPrice());
        assertEquals(20, product.getInventoryCount());
        assertEquals("http://voorbeeld.nl/geupdateafbeeldinglerenschoenen.jpg", product.getImageUrl());
        assertEquals("testomschrijving-geupdate", product.getProductDescription());
        assertEquals("Leer", product.getMaterial());
    }
}