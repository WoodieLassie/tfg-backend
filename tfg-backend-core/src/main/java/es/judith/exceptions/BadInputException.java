package es.judith.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid request body")
public class BadInputException extends RuntimeException {

  @Serial
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
