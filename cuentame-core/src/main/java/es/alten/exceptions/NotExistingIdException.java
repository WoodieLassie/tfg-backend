package es.alten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class NotExistingIdException extends RuntimeException {

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
