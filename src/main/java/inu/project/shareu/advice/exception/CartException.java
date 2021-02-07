package inu.project.shareu.advice.exception;

public class CartException extends RuntimeException {

    public CartException() {
        super();
    }

    public CartException(String message) {
        super(message);
    }

    public CartException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartException(Throwable cause) {
        super(cause);
    }
}
