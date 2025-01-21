package es.judith.exceptions;

import es.judith.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnhandledExceptionTest {

  UnhandledException unhandledException;

  @Test
  void testUnhandledException() {
    unhandledException = new UnhandledException();
    Assertions.assertEquals(
        unhandledException.getClass(), es.judith.exceptions.UnhandledException.class);
  }

  @Test
  void testUnhandledExceptionString() {
    unhandledException = new UnhandledException(Constants.ERROR);
    assertEquals(Constants.ERROR, unhandledException.getMessage());
  }

  @Test
  void testUnhandledExceptionStringThrowable() {
    Throwable t = new Throwable();
    unhandledException = new UnhandledException(Constants.ERROR, t);
    assertEquals(Constants.ERROR, unhandledException.getMessage());
    assertEquals(unhandledException.getCause(), t);
  }

  @Test
  void testUnhandledExceptionThrowable() {
    Throwable t = new Throwable();
    unhandledException = new UnhandledException(t);
    assertEquals(unhandledException.getCause(), t);
  }
}
