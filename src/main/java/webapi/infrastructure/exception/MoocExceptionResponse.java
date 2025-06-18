package webapi.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoocExceptionResponse {
    private String message;
    private boolean success;

    public boolean getSuccess() {
        return success;
    }
}
