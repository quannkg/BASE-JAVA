package webapi.infrastructure.exception;

public class InvalidTimeException extends RuntimeException {
  public InvalidTimeException(String errorMsg) {
    super(errorMsg);
  }
}
