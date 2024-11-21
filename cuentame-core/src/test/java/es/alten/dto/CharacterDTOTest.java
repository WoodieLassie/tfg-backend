package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterDTOTest {
  CharacterDTO characterDTO = new CharacterDTO();

  @BeforeEach
  void setUp() {
    characterDTO = new CharacterDTO();
  }

  @Test
  void testEquals() {
    characterDTO.equals(null);
    Assertions.assertNotEquals(null, characterDTO);
  }

  @Test
  void testHashCode() {
    CharacterDTO x = new CharacterDTO();
    x.setName("name");
    CharacterDTO y = new CharacterDTO();
    y.setName("name");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
