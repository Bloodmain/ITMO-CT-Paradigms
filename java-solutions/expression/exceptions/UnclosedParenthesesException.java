package expression.exceptions;

public class UnclosedParenthesesException extends UnexpectedTokenException {
    public UnclosedParenthesesException(String message) {
        super(message);
    }

    public UnclosedParenthesesException(String message, Throwable cause) {
        super(message, cause);
    }
}
