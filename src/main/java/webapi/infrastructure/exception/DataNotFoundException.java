package webapi.infrastructure.exception;

public class DataNotFoundException  extends Exception{
    public DataNotFoundException(String message) {
        super(message);
    }
}