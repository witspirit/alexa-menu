package be.witspirit.amazonlogin;

/**
 * Indicates that the provided token was invalid
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Invalid Token");
    }
}
