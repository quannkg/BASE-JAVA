package webapi.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CreateCourseExceptionResponse {
  private Integer statusCode;
  private Instant timestamp;
  private String path;
  private String message;
}
