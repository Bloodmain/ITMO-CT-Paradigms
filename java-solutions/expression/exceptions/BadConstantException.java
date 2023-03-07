package expression.exceptions;

public class BadConstantException extends ParseException {

    public BadConstantException(String message) {
        super(message);
    }

    public BadConstantException(String message, Throwable cause) {
        super(message, cause);
    }
}
