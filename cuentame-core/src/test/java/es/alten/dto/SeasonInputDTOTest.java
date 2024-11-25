package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeasonInputDTOTest {
  SeasonInputDTO seasonInputDTO = new SeasonInputDTO();

  @BeforeEach
  void setUp() {
    seasonInputDTO = new SeasonInputDTO();
  }

  @Test
  void testEquals() {
    seasonInputDTO.equals(null);
    Assertions.assertNotEquals(null, seasonInputDTO);
  }

  @Test
  void testHashCode() {
    SeasonInputDTO x = new SeasonInputDTO();
    x.setDescription("desc");
    SeasonInputDTO y = new SeasonInputDTO();
    y.setDescription("desc");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  void testAllFieldsArePresent() {
    SeasonInputDTO x = new SeasonInputDTO();
    SeasonInputDTO y = new SeasonInputDTO();
    y.setDescription("desc");
    y.setId(1L);
    y.setSeasonNum(1);
    Assertions.assertFalse(x.allFieldsArePresent());
    Assertions.assertTrue(y.allFieldsArePresent());
  }
}
