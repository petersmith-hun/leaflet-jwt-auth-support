package hu.psprog.leaflet.jwt.auth.support.logout;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.jwt.auth.support.exception.LogoutFailedException;
import hu.psprog.leaflet.jwt.auth.support.service.JWTAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logout handler to revoke JWT authentication token on logout.
 *
 * @author Peter Smith
 */
@Component
public class TokenRevokeLogoutHandler implements LogoutHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRevokeLogoutHandler.class);
    private static final String BRIDGE_COULD_NOT_REACH_LEAFLET = "Bridge could not reach Leaflet backend application for authentication.";

    private JWTAuthenticationService jwtAuthenticationService;

    @Autowired
    public TokenRevokeLogoutHandler(JWTAuthenticationService jwtAuthenticationService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        try {
            jwtAuthenticationService.revokeToken();
        } catch (CommunicationFailureException e) {
            LOGGER.error(BRIDGE_COULD_NOT_REACH_LEAFLET, e);
            throw new LogoutFailedException(e);
        }
    }
}
