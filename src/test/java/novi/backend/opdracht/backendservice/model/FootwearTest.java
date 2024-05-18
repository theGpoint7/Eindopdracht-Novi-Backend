package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDTO;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FootwearTest {

    private Footwear footwear;

    @BeforeEach
    public void setUp() {
        footwear = new Footwear();
    }

    @Test
    public void testSetAndGetFootwearSize() {
        // Arrange
        Integer expectedFootwearSize = 42;

        // Act
        footwear.setFootwearSize(expectedFootwearSize);
        Integer actualFootwearSize = footwear.getFootwearSize();

        // Assert
        assertEquals(expectedFootwearSize, actualFootwearSize);
    }

    @Test
    public void testSetAndGetGender() {
        // Arrange
        String expectedGender = "Man";

        // Act
        footwear.setGender(expectedGender);
        String actualGender = footwear.getGender();

        // Assert
        assertEquals(expectedGender, actualGender);
    }

    @Test
    public void testInsertSpecificFields() {
        // Arrange
        ProductInputDTO productInputDTO = new ProductInputDTO();
        productInputDTO.setFootwearSize(38);
        productInputDTO.setGender("Vrouw");

        // Act
        footwear.insertSpecificFields(productInputDTO);

        // Assert
        assertEquals(38, footwear.getFootwearSize());
        assertEquals("Vrouw", footwear.getGender());
    }

    @Test
    public void testUpdateSpecificFields() {
        // Arrange
        ProductUpdateDTO productUpdateDTO = new ProductUpdateDTO();
        productUpdateDTO.setFootwearSize(40);
        productUpdateDTO.setGender("Unisex");

        // Act
        footwear.updateSpecificFields(productUpdateDTO);

        // Assert
        assertEquals(40, footwear.getFootwearSize());
        assertEquals("Unisex", footwear.getGender());
    }
}
