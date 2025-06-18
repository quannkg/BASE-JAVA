package webapi.infrastructure.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Log4j2
public class ApiExceptionHandler {
  @ExceptionHandler(value = {AppException.class})
  public static ResponseEntity<Object> serverError(AppException e) {
    ApiRequestException apiException =
        new ApiRequestException(
            e.getMessage(),
            e.getStatus(),
            e.getStatus().value(),
            ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(apiException, e.getStatus());
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<MoocExceptionResponse> unknownExceptionHandler(Exception exception) {
    log.error(" > ERROR: ", exception);
    return new ResponseEntity<>(
        MoocExceptionResponse.builder()
            .success(false)
            .message("Có lỗi xảy ra. Vui lòng thử lại")
            .build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {NotAuthorizedException.class, AccessDeniedException.class})
  public ResponseEntity<MoocExceptionResponse> notAuthorizedExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = ForbiddenException.class)
  public ResponseEntity<MoocExceptionResponse> forbiddenExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<MoocExceptionResponse> notFoundExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String errorMessage = error.getDefaultMessage();
              errors.put("message", errorMessage);
              errors.put("success", false);
            });
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(value = {InputCustomValidatorException.class, CookieDecryptException.class})
  public ResponseEntity<MoocExceptionResponse> inputCustomValidatorExceptionHandler(
      Exception exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = CommentValidatorException.class)
  public ResponseEntity<MoocExceptionResponse> commentValidatorExceptionHandler(
      CommentValidatorException exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        exception.getHttpStatus());
  }

  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<MoocExceptionResponse> badRequestExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = GeneralException.class)
  public ResponseEntity<MoocGeneralExceptionResponse> generalExceptionHandler(
      GeneralException exception) {
    return new ResponseEntity<>(
        MoocGeneralExceptionResponse.builder()
            .success(false)
            .messageCode(exception.getMessageCode())
            .message(exception.getMessage())
            .build(),
        HttpStatus.valueOf(exception.getStatusCode()));
  }

  @ExceptionHandler(value = LoginFailException.class)
  public ResponseEntity<LoginExceptionResponse> handleLoginFailException(
      LoginFailException exception) {
    return new ResponseEntity<>(
        LoginExceptionResponse.builder()
            .message(exception.getMessage())
            .retryTime(exception.getRetryTime())
            .remainingLockSeconds(exception.getRemainingLockSeconds())
            .maxRetryTime(exception.getMaxRetryCount())
            .build(),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(value = InvalidTimeException.class)
  public ResponseEntity<MoocExceptionResponse> invalidTimeExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        MoocExceptionResponse.builder().success(false).message(exception.getMessage()).build(),
        HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = CourseValidatorException.class)
  public ResponseEntity<CreateCourseExceptionResponse> courseValidatorExceptionHandler(
      CourseValidatorException exception) {
    return new ResponseEntity<>(
        CreateCourseExceptionResponse.builder()
            .statusCode(exception.getHttpStatus().value())
            .message(exception.getMessage())
            .path(exception.getPathUrl())
            .timestamp(Instant.now().plusSeconds(7 * 60 * 60))
            .build(),
        exception.getHttpStatus());
  }
}
