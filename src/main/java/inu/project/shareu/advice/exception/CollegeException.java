package inu.project.shareu.advice.exception;

public class CollegeException extends RuntimeException{

    public CollegeException() {
        super();
    }

    public CollegeException(String message) {
        super(message);
    }

    public CollegeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollegeException(Throwable cause) {
        super(cause);
    }
}
