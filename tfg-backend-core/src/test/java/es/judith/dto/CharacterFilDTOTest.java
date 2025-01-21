package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterFilDTOTest {
  CharacterFilterDTO characterFilterDTO;

  @BeforeEach
  void setUp() {
    characterFilterDTO = new CharacterFilterDTO();
  }

  @Test
  void testObtainFilterSpecification() {
    Assertions.assertNull(characterFilterDTO.obtainFilterSpecification());
  }
}
