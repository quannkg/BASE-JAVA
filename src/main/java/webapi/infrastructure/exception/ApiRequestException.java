package webapi.infrastructure.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
public class ApiRequestException {
    private final String message;
    private final HttpStatus httpStatus;
    private final int codeStatus;
    private final ZonedDateTime zonedDateTime;

}
