package es.alten.exceptions;

public class UnhandledException extends RuntimeException {

  /** serialVersionUID for object serialization. */
  private static final long serialVersionUID = -3359819502876027977L;

  public UnhandledException() {
    super();
  }

  public UnhandledException(String message) {
    super(message);
  }

  public UnhandledException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnhandledException(Throwable cause) {
    super(cause);
  }
}
