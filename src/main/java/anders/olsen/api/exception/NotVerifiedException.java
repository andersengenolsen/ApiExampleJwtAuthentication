package anders.olsen.api.exception;

/**
 * Thrown when e-mail is not verified..
 */
public class NotVerifiedException extends RuntimeException {

    public NotVerifiedException(String message) {
        super(message);
    }

    public NotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
