package es.alten.exceptions;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlreadyExistsExceptionTest {

  AlreadyExistsException alreadyExistsException;

  @Test
  void testAlreadyExistsException() {
    alreadyExistsException = new AlreadyExistsException();
    Assertions.assertEquals(alreadyExistsException.getClass(), es.alten.exceptions.AlreadyExistsException.class);
  }

  @Test
  void testAlreadyExistsExceptionString() {
    alreadyExistsException = new AlreadyExistsException(Constants.ERROR);
    assertEquals(Constants.ERROR, alreadyExistsException.getMessage());
  }

  @Test
  void testAlreadyExistsExceptionStringThrowable() {
    Throwable t = new Throwable();
    alreadyExistsException = new AlreadyExistsException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, alreadyExistsException.getMessage());
    assertEquals(alreadyExistsException.getCause(), t);
  }

  @Test
  void testAlreadyExistsExceptionThrowable() {
    Throwable t = new Throwable();
    alreadyExistsException = new AlreadyExistsException(t);
    assertEquals(alreadyExistsException.getCause(), t);
  }
}
