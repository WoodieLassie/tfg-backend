package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterNoActorDTOTest {
  CharacterNoActorsDTO characterNoActorsDTO = new CharacterNoActorsDTO();

  @BeforeEach
  void setUp() {
    characterNoActorsDTO = new CharacterNoActorsDTO();
  }

  @Test
  void testEquals() {
    characterNoActorsDTO.equals(null);
    Assertions.assertNotEquals(null, characterNoActorsDTO);
  }

  @Test
  void testHashCode() {
    CharacterNoActorsDTO x = new CharacterNoActorsDTO();
    x.setName("name");
    CharacterNoActorsDTO y = new CharacterNoActorsDTO();
    y.setName("name");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
