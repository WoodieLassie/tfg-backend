package es.judith.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenericEntityTest {

  GenericEntity genericEntity;

  @BeforeEach
  void setUp() {
    genericEntity = new GenericEntity() {};
  }

  @Test
  void testEquals() {
    genericEntity.equals(null);
    Assertions.assertNotEquals(null, genericEntity);
  }

  @Test
  void testHashCode() {
    Assertions.assertEquals(genericEntity.hashCode(), genericEntity.hashCode());
  }
}
