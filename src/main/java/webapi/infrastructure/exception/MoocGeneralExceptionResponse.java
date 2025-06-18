package webapi.infrastructure.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoocGeneralExceptionResponse {
    private String message;
    private boolean success;
    private String messageCode;
    public boolean getSuccess() {
        return success;
    }
}
