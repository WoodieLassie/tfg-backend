package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeasonDTOTest {
  SeasonDTO seasonDTO = new SeasonDTO();

  @BeforeEach
  void setUp() {
    seasonDTO = new SeasonDTO();
  }

  @Test
  void testEquals() {
    seasonDTO.equals(null);
    Assertions.assertNotEquals(null, seasonDTO);
  }

  @Test
  void testHashCode() {
    SeasonDTO x = new SeasonDTO();
    x.setDescription("desc");
    SeasonDTO y = new SeasonDTO();
    y.setDescription("desc");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
