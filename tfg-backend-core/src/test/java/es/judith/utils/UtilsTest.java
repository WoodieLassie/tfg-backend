package es.judith.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest {

  Utils utils;

  @Test
  void test() {
    utils = new Utils();
    Assertions.assertEquals(utils.getClass(), Utils.class);
  }
}
