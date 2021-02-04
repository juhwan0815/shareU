package inu.project.shareu.advice.exception;

public class AuthenticationEntryPointException extends RuntimeException{

    public AuthenticationEntryPointException() {
        super();
    }

    public AuthenticationEntryPointException(String message) {
        super(message);
    }

    public AuthenticationEntryPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationEntryPointException(Throwable cause) {
        super(cause);
    }
}
