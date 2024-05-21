package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccessoryTest {

    private Accessory accessory;

    @BeforeEach
    public void setUp() {
        accessory = new Accessory();
    }

    @Test
    public void testConstructor() {
        Accessory accessory = new Accessory();
        assertNotNull(accessory);
    }

    @Test
    public void testSetAndGetAccessoryType() {
        AccessoryType expectedAccessoryType = AccessoryType.BAG;
        accessory.setAccessoryType(expectedAccessoryType);
        AccessoryType actualAccessoryType = accessory.getAccessoryType();
        assertEquals(expectedAccessoryType, actualAccessoryType);
    }

    @Test
    public void testInsertSpecificFields() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setAccessoryType("BELT");
        accessory.insertSpecificFields(productInputDTO);
        assertEquals(AccessoryType.BELT, accessory.getAccessoryType());
    }

    @Test
    public void testInsertSpecificFieldsInvalidType() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setAccessoryType("INVALID_TYPE");
        assertThrows(IllegalArgumentException.class, () -> {
            accessory.insertSpecificFields(productInputDTO);
        });
    }

    @Test
    public void testUpdateSpecificFields() {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();
        productUpdateDTO.setAccessoryType("HAT");
        accessory.updateSpecificFields(productUpdateDTO);
        assertEquals(AccessoryType.HAT, accessory.getAccessoryType());
    }

    @Test
    public void testUpdateSpecificFieldsInvalidType() {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();
        productUpdateDTO.setAccessoryType("INVALID_TYPE");
        assertThrows(IllegalArgumentException.class, () -> {
            accessory.updateSpecificFields(productUpdateDTO);
        });
    }

    @Test
    public void testSetAndGetProductName() {
        String expectedProductName = "TestProduct";
        accessory.setProductName(expectedProductName);
        String actualProductName = accessory.getProductName();
        assertEquals(expectedProductName, actualProductName);
    }

    @Test
    public void testSetAndGetProductType() {
        String expectedProductType = "Accessory";
        accessory.setProductType(expectedProductType);
        String actualProductType = accessory.getProductType();
        assertEquals(expectedProductType, actualProductType);
    }

    @Test
    public void testSetAndGetPrice() {
        double expectedPrice = 100.0;
        accessory.setPrice(expectedPrice);
        double actualPrice = accessory.getPrice();
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    public void testSetAndGetInventoryCount() {
        int expectedInventoryCount = 10;
        accessory.setInventoryCount(expectedInventoryCount);
        int actualInventoryCount = accessory.getInventoryCount();
        assertEquals(expectedInventoryCount, actualInventoryCount);
    }

    @Test
    public void testSetAndGetImageUrl() {
        String expectedImageUrl = "http://example.com/image.jpg";
        accessory.setImageUrl(expectedImageUrl);
        String actualImageUrl = accessory.getImageUrl();
        assertEquals(expectedImageUrl, actualImageUrl);
    }

    @Test
    public void testSetAndGetProductDescription() {
        String expectedProductDescription = "Test product.";
        accessory.setProductDescription(expectedProductDescription);
        String actualProductDescription = accessory.getProductDescription();
        assertEquals(expectedProductDescription, actualProductDescription);
    }

    @Test
    public void testSetAndGetMaterial() {
        String expectedMaterial = "Leather";
        accessory.setMaterial(expectedMaterial);
        String actualMaterial = accessory.getMaterial();
        assertEquals(expectedMaterial, actualMaterial);
    }

    @Test
    public void testSetAndGetDesigner() {
        Designer expectedDesigner = new Designer();
        accessory.setDesigner(expectedDesigner);
        Designer actualDesigner = accessory.getDesigner();
        assertEquals(expectedDesigner, actualDesigner);
    }

    @Test
    public void testSetAndGetPromotion() {
        Promotion expectedPromotion = new Promotion();
        accessory.setPromotion(expectedPromotion);
        Promotion actualPromotion = accessory.getPromotion();
        assertEquals(expectedPromotion, actualPromotion);
    }

    @Test
    public void testInsertCommonFields() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setProductName("testproduct-naam");
        productInputDTO.setProductType("Accessory");
        productInputDTO.setPrice(200.0);
        productInputDTO.setInventoryCount(20);
        productInputDTO.setImageUrl("http://voorbeeld.nl/afbeeldingzijdeschoenen.jpg");
        productInputDTO.setProductDescription("testomschrijving.");
        productInputDTO.setMaterial("zijde");

        accessory.insertCommonFields(productInputDTO);

        assertEquals("testproduct-naam", accessory.getProductName());
        assertEquals("Accessory", accessory.getProductType());
        assertEquals(200.0, accessory.getPrice());
        assertEquals(20, accessory.getInventoryCount());
        assertEquals("http://voorbeeld.nl/afbeeldingzijdeschoenen.jpg", accessory.getImageUrl());
        assertEquals("testomschrijving.", accessory.getProductDescription());
        assertEquals("zijde", accessory.getMaterial());
    }

    @Test
    public void testUpdateCommonFields() {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();
        productUpdateDTO.setProductName("Testproductnaam-geupdate");
        productUpdateDTO.setPrice(200.0);
        productUpdateDTO.setInventoryCount(20);
        productUpdateDTO.setImageUrl("http://voorbeeld.nl/geupdateafbeeldinglerenschoenen.jpg");
        productUpdateDTO.setProductDescription("testomschrijving-geupdate");
        productUpdateDTO.setMaterial("Leer");

        accessory.updateCommonFields(productUpdateDTO);

        assertEquals("Testproductnaam-geupdate", accessory.getProductName());
        assertEquals(200.0, accessory.getPrice());
        assertEquals(20, accessory.getInventoryCount());
        assertEquals("http://voorbeeld.nl/geupdateafbeeldinglerenschoenen.jpg", accessory.getImageUrl());
        assertEquals("testomschrijving-geupdate", accessory.getProductDescription());
        assertEquals("Leer", accessory.getMaterial());
    }
}
