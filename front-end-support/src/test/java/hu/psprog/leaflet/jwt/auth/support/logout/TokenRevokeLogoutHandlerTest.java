package hu.psprog.leaflet.jwt.auth.support.logout;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.jwt.auth.support.exception.LogoutFailedException;
import hu.psprog.leaflet.jwt.auth.support.exception.TokenAuthenticationFailureException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
@RunWith(MockitoJUnitRunner.class)
public class TokenRevokeLogoutHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TokenRevokeLogoutHandler tokenRevokeLogoutHandler;

    @Test
    public void shouldCallRevokeTokenEndpoint() throws CommunicationFailureException {

        // when
        tokenRevokeLogoutHandler.logout(request, response, authentication);

        // then
        verify(authenticationService).revokeToken();
    }

    @Test(expected = LogoutFailedException.class)
    public void shouldThrowCouldNotReachBackendExceptionOnCommunicationFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(authenticationService).revokeToken();

        // when
        tokenRevokeLogoutHandler.logout(request, response, authentication);

        // then
        // exception expected
    }
}
