package es.alten.exceptions;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotExistingIdExceptionTest {

  NotExistingIdException NotExistingIdException;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  void testNotExistingIdException() {
    NotExistingIdException = new NotExistingIdException();
    Assertions.assertEquals(
        NotExistingIdException.getClass(), es.alten.exceptions.NotExistingIdException.class);
  }

  @Test
  void testNotExistingIdExceptionString() {
    NotExistingIdException = new NotExistingIdException(Constants.ERROR);
    assertEquals(Constants.ERROR, NotExistingIdException.getMessage());
  }

  @Test
  void testNotExistingIdExceptionStringThrowable() {
    Throwable t = new Throwable();
    NotExistingIdException = new NotExistingIdException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, NotExistingIdException.getMessage());
    assertEquals(NotExistingIdException.getCause(), t);
  }

  @Test
  void testNotExistingIdExceptionThrowable() {
    Throwable t = new Throwable();
    NotExistingIdException = new NotExistingIdException(t);
    assertEquals(NotExistingIdException.getCause(), t);
  }
}
