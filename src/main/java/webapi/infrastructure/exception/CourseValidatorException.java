package webapi.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CourseValidatorException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String pathUrl;

  public CourseValidatorException(String errorMsg, String pathUrl, HttpStatus httpStatus) {
    super(errorMsg);
    this.httpStatus = httpStatus;
    this.pathUrl = pathUrl;
  }
}
