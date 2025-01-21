package es.judith.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadInputException extends RuntimeException {

  /** serialVersionUID for object serialization. */
  private static final long serialVersionUID = 1941351070214026991L;

  public BadInputException() {
    super();
  }

  public BadInputException(String message) {
    super(message);
  }

  public BadInputException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadInputException(Throwable cause) {
    super(cause);
  }
}
