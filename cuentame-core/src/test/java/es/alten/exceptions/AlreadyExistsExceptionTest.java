package es.alten.exceptions;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlreadyExistsExceptionTest {

  AlreadyExistsException AlreadyExistsException;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  void testAlreadyExistsException() {
    AlreadyExistsException = new AlreadyExistsException();
    Assertions.assertEquals(AlreadyExistsException.getClass(), es.alten.exceptions.AlreadyExistsException.class);
  }

  @Test
  void testAlreadyExistsExceptionString() {
    AlreadyExistsException = new AlreadyExistsException(Constants.ERROR);
    assertEquals(Constants.ERROR, AlreadyExistsException.getMessage());
  }

  @Test
  void testAlreadyExistsExceptionStringThrowable() {
    Throwable t = new Throwable();
    AlreadyExistsException = new AlreadyExistsException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, AlreadyExistsException.getMessage());
    assertEquals(AlreadyExistsException.getCause(), t);
  }

  @Test
  void testAlreadyExistsExceptionThrowable() {
    Throwable t = new Throwable();
    AlreadyExistsException = new AlreadyExistsException(t);
    assertEquals(AlreadyExistsException.getCause(), t);
  }
}
