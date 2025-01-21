package es.judith.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorFilDTOTest {
  ActorFilterDTO actorFilterDTO;

  @BeforeEach
  void setUp() {
    actorFilterDTO = new ActorFilterDTO();
  }

  @Test
  void testObtainFilterSpecification() {
    Assertions.assertNull(actorFilterDTO.obtainFilterSpecification());
  }
}
