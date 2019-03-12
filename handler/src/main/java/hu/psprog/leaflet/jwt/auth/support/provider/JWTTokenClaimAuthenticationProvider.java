package hu.psprog.leaflet.jwt.auth.support.provider;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.jwt.auth.support.exception.TokenAuthenticationFailureException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Standard Spring Security {@link AuthenticationProvider} implementation which handles JWT based authentication for front-end applications.
 * This provider calls token claim service under Leaflet backend application through Bridge.
 *
 * @author Peter Smith
 */
@Component
public class JWTTokenClaimAuthenticationProvider implements AuthenticationProvider {

    private static final String AUTHENTICATION_VIA_BRIDGE_FAILED = "Authentication via Bridge failed.";

    private AuthenticationService authenticationService;

    @Autowired
    public JWTTokenClaimAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            return authenticationService.claimToken(authentication);
        } catch (UnauthorizedAccessException | CommunicationFailureException e) {
            throw new TokenAuthenticationFailureException(AUTHENTICATION_VIA_BRIDGE_FAILED, e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
