package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDTOTest {

  UserDTO userDTO = new UserDTO();

  @BeforeEach
  void setUp() throws Exception {
    userDTO = new UserDTO();
  }

  @Test
  void testEquals() {
    userDTO.equals(null);
    Assertions.assertNotEquals(null, userDTO);
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
}
