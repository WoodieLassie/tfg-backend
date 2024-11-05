package es.alten.exceptions;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {

  NotFoundException NotFoundException;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  void testNotFoundException() {
    NotFoundException = new NotFoundException();
    Assertions.assertEquals(
        NotFoundException.getClass(), es.alten.exceptions.NotFoundException.class);
  }

  @Test
  void testNotFoundExceptionString() {
    NotFoundException = new NotFoundException(Constants.ERROR);
    assertEquals(Constants.ERROR, NotFoundException.getMessage());
  }

  @Test
  void testNotFoundExceptionStringThrowable() {
    Throwable t = new Throwable();
    NotFoundException = new NotFoundException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, NotFoundException.getMessage());
    assertEquals(NotFoundException.getCause(), t);
  }

  @Test
  void testNotFoundExceptionThrowable() {
    Throwable t = new Throwable();
    NotFoundException = new NotFoundException(t);
    assertEquals(NotFoundException.getCause(), t);
  }
}
