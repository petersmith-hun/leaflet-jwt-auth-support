package hu.psprog.leaflet.jwt.auth.support.logout;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.jwt.auth.support.exception.LogoutFailedException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TokenRevokeLogoutHandler}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TokenRevokeLogoutHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ForcedLogoutHandler forcedLogoutHandler;

    @InjectMocks
    private TokenRevokeLogoutHandler tokenRevokeLogoutHandler;

    @Test
    public void shouldCallRevokeTokenEndpoint() throws CommunicationFailureException {

        // when
        tokenRevokeLogoutHandler.logout(request, response, authentication);

        // then
        verify(authenticationService).revokeToken();
    }

    @Test
    public void shouldCallForceLogoutInCaseUnauthorizedExceptionIsThrown() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(authenticationService).revokeToken();

        // when
        tokenRevokeLogoutHandler.logout(request, response, authentication);

        // then
        verify(forcedLogoutHandler).forceLogout(request);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldThrowCouldNotReachBackendExceptionOnCommunicationFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(authenticationService).revokeToken();

        // when
        Assertions.assertThrows(LogoutFailedException.class, () -> tokenRevokeLogoutHandler.logout(request, response, authentication));

        // then
        // exception expected
    }
}
