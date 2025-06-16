package webapi.infrastructure.exception;

public class NotAuthorizedException extends RuntimeException {
  public NotAuthorizedException(String errorMsg) {
    super(errorMsg);
  }
}
