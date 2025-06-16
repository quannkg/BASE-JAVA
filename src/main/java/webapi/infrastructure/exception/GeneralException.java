package webapi.infrastructure.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private Integer statusCode;
    private String messageCode;

    public GeneralException(String errorMsg, Integer statusCode, String messageCode) {
        super(errorMsg);
        this.statusCode = statusCode;
        this.messageCode = messageCode;
    }
}
