package webapi.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommentValidatorException extends RuntimeException {
    private final HttpStatus httpStatus;
    public CommentValidatorException(String errorMsg , HttpStatus httpStatus) {
        super(errorMsg);
        this.httpStatus = httpStatus;
    }
}
