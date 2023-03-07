package expression.exceptions;

public class UnavailableIdentifierException extends UnexpectedTokenException {

    public UnavailableIdentifierException(String message) {
        super(message);
    }

    public UnavailableIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
