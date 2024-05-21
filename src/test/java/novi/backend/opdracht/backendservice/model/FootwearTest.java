package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.ProductInputDto;
import novi.backend.opdracht.backendservice.dto.input.ProductUpdateDto;
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
        Integer expectedFootwearSize = 42;
        footwear.setFootwearSize(expectedFootwearSize);
        Integer actualFootwearSize = footwear.getFootwearSize();
        assertEquals(expectedFootwearSize, actualFootwearSize);
    }

    @Test
    public void testSetAndGetGender() {
        String expectedGender = "Man";
        footwear.setGender(expectedGender);
        String actualGender = footwear.getGender();
        assertEquals(expectedGender, actualGender);
    }

    @Test
    public void testInsertSpecificFields() {
        ProductInputDto productInputDTO = new ProductInputDto();
        productInputDTO.setFootwearSize(38);
        productInputDTO.setGender("Vrouw");
        footwear.insertSpecificFields(productInputDTO);
        assertEquals(38, footwear.getFootwearSize());
        assertEquals("Vrouw", footwear.getGender());
    }

    @Test
    public void testUpdateSpecificFields() {
        ProductUpdateDto productUpdateDTO = new ProductUpdateDto();
        productUpdateDTO.setFootwearSize(40);
        productUpdateDTO.setGender("Unisex");
        footwear.updateSpecificFields(productUpdateDTO);
        assertEquals(40, footwear.getFootwearSize());
        assertEquals("Unisex", footwear.getGender());
    }
}
