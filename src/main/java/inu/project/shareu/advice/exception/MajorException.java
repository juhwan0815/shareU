package inu.project.shareu.advice.exception;

public class MajorException extends RuntimeException {

    public MajorException() {
        super();
    }

    public MajorException(String message) {
        super(message);
    }

    public MajorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MajorException(Throwable cause) {
        super(cause);
    }
}
