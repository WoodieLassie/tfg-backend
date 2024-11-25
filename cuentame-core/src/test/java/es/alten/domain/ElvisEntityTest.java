package es.alten.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElvisEntityTest {

  ElvisEntity elvisEntity;

  @BeforeEach
  void setUp() {
    elvisEntity = new ElvisEntity() {};
  }

  @Test
  void testEquals() {
    elvisEntity.equals(null);
    Assertions.assertNotEquals(null, elvisEntity);
  }

  @Test
  void testHashCode() {
    Assertions.assertEquals(elvisEntity.hashCode(), elvisEntity.hashCode());
  }
}
