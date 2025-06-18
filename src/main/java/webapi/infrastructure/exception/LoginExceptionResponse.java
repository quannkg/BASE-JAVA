package webapi.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginExceptionResponse {
  private String message;

  @Builder.Default private boolean success = false;
  private Integer retryTime;

  @Builder.Default private Integer remainingLockSeconds = 0;

  private Integer maxRetryTime;

}
