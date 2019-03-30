package hu.psprog.leaflet.jwt.auth.support.logout;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.jwt.auth.support.exception.LogoutFailedException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
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
    private static final String SESSION_HAS_ALREADY_BEEN_INVALIDATED = "Session has already been invalidated - forcing logout";

    private AuthenticationService authenticationService;
    private ForcedLogoutHandler forcedLogoutHandler;

    @Autowired
    public TokenRevokeLogoutHandler(AuthenticationService authenticationService, ForcedLogoutHandler forcedLogoutHandler) {
        this.authenticationService = authenticationService;
        this.forcedLogoutHandler = forcedLogoutHandler;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        try {
            authenticationService.revokeToken();
        } catch (UnauthorizedAccessException e) {
            forceLogout(request, response, e);
        } catch (CommunicationFailureException e) {
            LOGGER.error(BRIDGE_COULD_NOT_REACH_LEAFLET, e);
            throw new LogoutFailedException(e);
        }
    }

    private void forceLogout(HttpServletRequest request, HttpServletResponse response, UnauthorizedAccessException e) {

        LOGGER.warn(SESSION_HAS_ALREADY_BEEN_INVALIDATED, e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        forcedLogoutHandler.forceLogout(request);
    }
}
