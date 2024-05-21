package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClothingTest {

    private Clothing clothing;

    @BeforeEach
    public void setUp() {
        clothing = new Clothing();
    }

    @Test
    public void testSetAndGetClothingSize() {
        String expectedClothingSize = "M";
        clothing.setClothingSize(expectedClothingSize);
        String actualClothingSize = clothing.getClothingSize();
        assertEquals(expectedClothingSize, actualClothingSize);
    }

    @Test
    public void testSetAndGetColor() {
        String expectedColor = "Rood";
        clothing.setColor(expectedColor);
        String actualColor = clothing.getColor();
        assertEquals(expectedColor, actualColor);
    }

    @Test
    public void testSetAndGetFit() {
        String expectedFit = "Slim-fit";
        clothing.setFit(expectedFit);
        String actualFit = clothing.getFit();
        assertEquals(expectedFit, actualFit);
    }

    @Test
    public void testInsertSpecificFields() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setClothingSize("L");
        productInputDTO.setColor("Blauw");
        productInputDTO.setFit("Normaal");
        clothing.insertSpecificFields(productInputDTO);
        assertEquals("L", clothing.getClothingSize());
        assertEquals("Blauw", clothing.getColor());
        assertEquals("Normaal", clothing.getFit());
    }

    @Test
    public void testUpdateSpecificFields() {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();
        productUpdateDTO.setClothingSize("S");
        productUpdateDTO.setColor("Groen");
        productUpdateDTO.setFit("Loose-fit");
        clothing.updateSpecificFields(productUpdateDTO);
        assertEquals("S", clothing.getClothingSize());
        assertEquals("Groen", clothing.getColor());
        assertEquals("Loose-fit", clothing.getFit());
    }
}
