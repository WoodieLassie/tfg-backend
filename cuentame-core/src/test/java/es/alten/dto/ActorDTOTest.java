package es.alten.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorDTOTest {
  ActorDTO actorDTO = new ActorDTO();

  @BeforeEach
  void setUp() {
    actorDTO = new ActorDTO();
  }

  @Test
  void testEquals() {
    actorDTO.equals(null);
    Assertions.assertNotEquals(null, actorDTO);
  }

  @Test
  void testHashCode() {
    ActorDTO x = new ActorDTO();
    x.setName("actor");
    ActorDTO y = new ActorDTO();
    y.setName("actor");
    Assertions.assertTrue(x.equals(y) && y.equals(x));
    Assertions.assertEquals(x.hashCode(), y.hashCode());
  }
}
