package hu.psprog.leaflet.jwt.auth.support.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Peter Smith
 */
public class TokenAuthenticationFailureException extends AuthenticationException {

    public TokenAuthenticationFailureException(String msg) {
        super(msg);
    }

    public TokenAuthenticationFailureException(String msg, Throwable t) {
        super(msg, t);
    }
}
