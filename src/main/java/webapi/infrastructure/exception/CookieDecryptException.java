package webapi.infrastructure.exception;

public class CookieDecryptException extends RuntimeException {
  public CookieDecryptException(String errorMsg) {
    super(errorMsg);
  }
}
