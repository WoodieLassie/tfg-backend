package es.alten.exceptions;

import es.alten.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnhandledExceptionTest {

  UnhandledException UnhandledException;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  void testUnhandledException() {
    UnhandledException = new UnhandledException();
    Assertions.assertEquals(
        UnhandledException.getClass(), es.alten.exceptions.UnhandledException.class);
  }

  @Test
  void testUnhandledExceptionString() {
    UnhandledException = new UnhandledException(Constants.ERROR);
    assertEquals(Constants.ERROR, UnhandledException.getMessage());
  }

  @Test
  void testUnhandledExceptionStringThrowable() {
    Throwable t = new Throwable();
    UnhandledException = new UnhandledException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, UnhandledException.getMessage());
    assertEquals(UnhandledException.getCause(), t);
  }

  @Test
  void testUnhandledExceptionThrowable() {
    Throwable t = new Throwable();
    UnhandledException = new UnhandledException(t);
    assertEquals(UnhandledException.getCause(), t);
  }
}
