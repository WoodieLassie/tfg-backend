package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageFilDTOTest {
  ImageFilterDTO imageFilterDTO;

  @BeforeEach
  void setUp() {
    imageFilterDTO = new ImageFilterDTO();
  }

  @Test
  void testObtainFilterSpecification() {
    imageFilterDTO.obtainFilterSpecification();
    imageFilterDTO.setName("name");
    imageFilterDTO.setType("type");
    imageFilterDTO.setImageData("image".getBytes());
    Assertions.assertEquals("name", imageFilterDTO.getName());
    Assertions.assertEquals("type", imageFilterDTO.getType());
    Assertions.assertArrayEquals("image".getBytes(), imageFilterDTO.getImageData());
  }
}
