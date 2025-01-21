package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserInputDTOTest {
  UserInputDTO userInputDTO = new UserInputDTO();

  @BeforeEach
  void setUp() {
    userInputDTO = new UserInputDTO();
  }

  @Test
  void testEquals() {
    userInputDTO.equals(null);
    Assertions.assertNotEquals(null, userInputDTO);
  }

  @Test
  void testHashCode() {
    UserDTO x = new UserDTO();
    x.setEmail("Foo Bar");
    UserDTO y = new UserDTO();
    y.setEmail("Foo Bar");
    Assertions.assertEquals(true, x.equals(y) && y.equals(x));
    Assertions.assertEquals(true, x.hashCode() == y.hashCode());
  }

  @Test
  void testAllFieldsArePresent() {
    UserInputDTO x = new UserInputDTO();
    UserInputDTO y = new UserInputDTO();
    y.setEmail("email@gmail.com");
    y.setPassword("12345678");
    Assertions.assertFalse(x.allFieldsArePresent());
    Assertions.assertTrue(y.allFieldsArePresent());
  }
}
