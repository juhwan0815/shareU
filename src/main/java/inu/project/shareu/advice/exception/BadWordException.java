package inu.project.shareu.advice.exception;

public class BadWordException extends RuntimeException{

    public BadWordException() {
        super();
    }

    public BadWordException(String message) {
        super(message);
    }

    public BadWordException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadWordException(Throwable cause) {
        super(cause);
    }
}
