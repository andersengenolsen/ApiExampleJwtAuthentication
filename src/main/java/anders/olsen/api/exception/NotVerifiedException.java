package anders.olsen.api.exception;

public class NotVerifiedException extends RuntimeException {

    public NotVerifiedException(String message) {
        super(message);
    }

    public NotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
