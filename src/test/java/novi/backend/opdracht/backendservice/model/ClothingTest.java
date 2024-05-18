package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDTO;
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
        // Arrange
        String expectedClothingSize = "M";

        // Act
        clothing.setClothingSize(expectedClothingSize);
        String actualClothingSize = clothing.getClothingSize();

        // Assert
        assertEquals(expectedClothingSize, actualClothingSize);
    }

    @Test
    public void testSetAndGetColor() {
        // Arrange
        String expectedColor = "Rood";

        // Act
        clothing.setColor(expectedColor);
        String actualColor = clothing.getColor();

        // Assert
        assertEquals(expectedColor, actualColor);
    }

    @Test
    public void testSetAndGetFit() {
        // Arrange
        String expectedFit = "Slim-fit";

        // Act
        clothing.setFit(expectedFit);
        String actualFit = clothing.getFit();

        // Assert
        assertEquals(expectedFit, actualFit);
    }

    @Test
    public void testInsertSpecificFields() {
        // Arrange
        ProductInputDTO productInputDTO = new ProductInputDTO();
        productInputDTO.setClothingSize("L");
        productInputDTO.setColor("Blauw");
        productInputDTO.setFit("Normaal");

        // Act
        clothing.insertSpecificFields(productInputDTO);

        // Assert
        assertEquals("L", clothing.getClothingSize());
        assertEquals("Blauw", clothing.getColor());
        assertEquals("Normaal", clothing.getFit());
    }

    @Test
    public void testUpdateSpecificFields() {
        // Arrange
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();
        productUpdateDTO.setClothingSize("S");
        productUpdateDTO.setColor("Groen");
        productUpdateDTO.setFit("Loose-fit");

        // Act
        clothing.updateSpecificFields(productUpdateDTO);

        // Assert
        assertEquals("S", clothing.getClothingSize());
        assertEquals("Groen", clothing.getColor());
        assertEquals("Loose-fit", clothing.getFit());
    }
}
