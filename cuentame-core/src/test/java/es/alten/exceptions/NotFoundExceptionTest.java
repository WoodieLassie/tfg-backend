package es.alten.exceptions;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {

  NotFoundException notFoundException;

  @Test
  void testNotFoundException() {
    notFoundException = new NotFoundException();
    Assertions.assertEquals(
        notFoundException.getClass(), es.alten.exceptions.NotFoundException.class);
  }

  @Test
  void testNotFoundExceptionString() {
    notFoundException = new NotFoundException(Constants.ERROR);
    assertEquals(Constants.ERROR, notFoundException.getMessage());
  }

  @Test
  void testNotFoundExceptionStringThrowable() {
    Throwable t = new Throwable();
    notFoundException = new NotFoundException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, notFoundException.getMessage());
    assertEquals(notFoundException.getCause(), t);
  }

  @Test
  void testNotFoundExceptionThrowable() {
    Throwable t = new Throwable();
    notFoundException = new NotFoundException(t);
    assertEquals(notFoundException.getCause(), t);
  }
}
