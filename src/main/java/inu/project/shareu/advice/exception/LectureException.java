package inu.project.shareu.advice.exception;

public class LectureException extends RuntimeException{

    public LectureException() {
        super();
    }

    public LectureException(String message) {
        super(message);
    }

    public LectureException(String message, Throwable cause) {
        super(message, cause);
    }

    public LectureException(Throwable cause) {
        super(cause);
    }
}
