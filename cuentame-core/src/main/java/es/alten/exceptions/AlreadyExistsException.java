package es.alten.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = 3220735874915464565L;

  public AlreadyExistsException() {
    super();
  }

  public AlreadyExistsException(String message) {
    super(message);
  }

  public AlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
