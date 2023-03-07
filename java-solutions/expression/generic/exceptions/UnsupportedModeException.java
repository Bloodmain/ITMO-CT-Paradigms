package expression.generic.exceptions;

public class UnsupportedModeException extends Exception {

    public UnsupportedModeException(String message) {
        super(message);
    }

    public UnsupportedModeException(String message, Throwable cause) {
        super(message, cause);
    }
}
