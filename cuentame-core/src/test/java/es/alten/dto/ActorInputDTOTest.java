package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

class ActorInputDTOTest {
  ActorInputDTO actorInputDTO = new ActorInputDTO();

  @BeforeEach
  void setUp() {
    actorInputDTO = new ActorInputDTO();
  }

  @Test
  void testEquals() {
    actorInputDTO.equals(null);
    Assertions.assertNotEquals(null, actorInputDTO);
  }

  @Test
  void testHashCode() {
    ActorInputDTO x = new ActorInputDTO();
    x.setName("actor");
    ActorInputDTO y = new ActorInputDTO();
    y.setName("actor");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  void testAllFieldsArePresent() {
    ActorInputDTO x = new ActorInputDTO();
    ActorInputDTO y = new ActorInputDTO();
    y.setId(1L);
    y.setName("actor");
    y.setGender("gender");
    y.setBirthDate(new Date(1, 1, 1));
    y.setNationality("nationality");
    y.setBirthLocation("location");
    y.setCharacterId(1L);
    Assertions.assertFalse(x.allFieldsArePresent());
    Assertions.assertTrue(y.allFieldsArePresent());
  }
}
