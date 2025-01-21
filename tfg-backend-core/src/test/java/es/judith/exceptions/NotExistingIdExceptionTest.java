package es.judith.exceptions;

import es.judith.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotExistingIdExceptionTest {

  NotExistingIdException notExistingIdException;

  @Test
  void testNotExistingIdException() {
    notExistingIdException = new NotExistingIdException();
    Assertions.assertEquals(
        notExistingIdException.getClass(), es.judith.exceptions.NotExistingIdException.class);
  }

  @Test
  void testNotExistingIdExceptionString() {
    notExistingIdException = new NotExistingIdException(Constants.ERROR);
    assertEquals(Constants.ERROR, notExistingIdException.getMessage());
  }

  @Test
  void testNotExistingIdExceptionStringThrowable() {
    Throwable t = new Throwable();
    notExistingIdException = new NotExistingIdException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, notExistingIdException.getMessage());
    assertEquals(notExistingIdException.getCause(), t);
  }

  @Test
  void testNotExistingIdExceptionThrowable() {
    Throwable t = new Throwable();
    notExistingIdException = new NotExistingIdException(t);
    assertEquals(notExistingIdException.getCause(), t);
  }
}
