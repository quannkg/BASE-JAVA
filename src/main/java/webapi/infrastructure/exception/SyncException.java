package webapi.infrastructure.exception;

public class SyncException extends RuntimeException {
  public SyncException(String errorMsg) {
    super(errorMsg);
  }
}
