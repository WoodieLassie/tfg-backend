package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserFilDTOTest {

  UserFilterDTO userFilterDTO;

  @BeforeEach
  void setUp() throws Exception {
    userFilterDTO = new UserFilterDTO();
  }

  @Test
  void testObtainFilterSpecification() {
    userFilterDTO.obtainFilterSpecification();
    userFilterDTO.setEmail("test1");
    Assertions.assertEquals("test1", userFilterDTO.getEmail());
  }

}
