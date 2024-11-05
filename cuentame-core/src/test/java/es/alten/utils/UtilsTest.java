package es.alten.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilsTest {

  Utils utils;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  void test() {
    utils = new Utils();
    Assertions.assertEquals(utils.getClass(), Utils.class);
  }
}
