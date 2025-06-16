package webapi.infrastructure.exception;

public class InputCustomValidatorException extends RuntimeException {
  public InputCustomValidatorException(String errorMsg) {
    super(errorMsg);
  }
}
