package es.judith.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class NotExistingIdException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -3925483040688043396L;

  public NotExistingIdException() {
    super();
  }

  public NotExistingIdException(String message) {
    super(message);
  }

  public NotExistingIdException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotExistingIdException(Throwable cause) {
    super(cause);
  }
}
