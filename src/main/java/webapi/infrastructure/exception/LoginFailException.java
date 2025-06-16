package webapi.infrastructure.exception;

import lombok.Getter;

@Getter
public class LoginFailException extends RuntimeException {

  public LoginFailException(
      String errorMessage, Integer retryTime, Integer remainingLockSeconds, Integer maxRetryCount) {
    super(errorMessage);
    this.retryTime = retryTime;
    this.remainingLockSeconds = remainingLockSeconds;
    this.maxRetryCount = maxRetryCount;
  }

  private final Integer retryTime;
  private final Integer remainingLockSeconds;
  private final Integer maxRetryCount;
}
