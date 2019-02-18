package hu.psprog.leaflet.jwt.auth.support.exception;

/**
 * Exception to throw upon losing connection to Leaflet backend application.
 *
 * @author Peter Smith
 */
public class LogoutFailedException extends RuntimeException {

    private static final String CONNECTION_LOST = "Connection lost to Leaflet.";

    public LogoutFailedException(Throwable cause) {
        super(CONNECTION_LOST, cause);
    }
}
