package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageDTOTest {
  ImageDTO imageDTO = new ImageDTO();

  @BeforeEach
  void setUp() {
    imageDTO = new ImageDTO();
  }

  @Test
  void testEquals() {
    imageDTO.equals(null);
    Assertions.assertNotEquals(null, imageDTO);
  }

  @Test
  void testHashCode() {
    ImageDTO x = new ImageDTO();
    x.setName("name");
    ImageDTO y = new ImageDTO();
    y.setName("name");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
