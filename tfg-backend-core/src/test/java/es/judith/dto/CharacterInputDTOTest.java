package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterInputDTOTest {
  CharacterInputDTO characterInputDTO = new CharacterInputDTO();

  @BeforeEach
  void setUp() {
    characterInputDTO = new CharacterInputDTO();
  }

  @Test
  void testEquals() {
    characterInputDTO.equals(null);
    Assertions.assertNotEquals(null, characterInputDTO);
  }

  @Test
  void testHashCode() {
    CharacterInputDTO x = new CharacterInputDTO();
    x.setName("name");
    CharacterInputDTO y = new CharacterInputDTO();
    y.setName("name");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  void testAllFieldsArePresent() {
    CharacterInputDTO x = new CharacterInputDTO();
    CharacterInputDTO y = new CharacterInputDTO();
    y.setName("name");
    y.setId(1L);
    y.setAge(23);
    y.setGender("gender");
    y.setNationality("nationality");
    y.setDescription("description");
    Assertions.assertFalse(x.allFieldsArePresent());
    Assertions.assertTrue(y.allFieldsArePresent());
  }
}
