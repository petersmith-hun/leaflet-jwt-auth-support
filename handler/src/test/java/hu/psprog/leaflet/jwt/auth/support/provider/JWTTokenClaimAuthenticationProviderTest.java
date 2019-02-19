package hu.psprog.leaflet.jwt.auth.support.provider;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import hu.psprog.leaflet.jwt.auth.support.exception.TokenAuthenticationFailureException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link JWTTokenClaimAuthenticationProvider}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class JWTTokenClaimAuthenticationProviderTest {

    @Mock
    private UsernamePasswordAuthenticationToken authentication;

    @Mock
    private JWTTokenAuthentication jwtTokenAuthentication;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private JWTTokenClaimAuthenticationProvider jwtTokenClaimAuthenticationProvider;

    @Test
    public void shouldAuthenticate() throws CommunicationFailureException {

        // given
        given(authenticationService.claimToken(authentication)).willReturn(jwtTokenAuthentication);

        // when
        Authentication result = jwtTokenClaimAuthenticationProvider.authenticate(authentication);

        // then
        assertThat(result, equalTo(jwtTokenAuthentication));
    }

    @Test(expected = TokenAuthenticationFailureException.class)
    public void shouldThrowTokenAuthenticationFailureExceptionOnCommunicationFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(authenticationService).claimToken(authentication);

        // when
        jwtTokenClaimAuthenticationProvider.authenticate(authentication);

        // then
        // exception expected
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowAuthenticationExceptionOnAuthenticationFailure() throws CommunicationFailureException {

        // given
        doThrow(BadCredentialsException.class).when(authenticationService).claimToken(authentication);

        // when
        jwtTokenClaimAuthenticationProvider.authenticate(authentication);

        // then
        // exception expected
    }

    @Test
    public void shouldSupportUsernamePasswordAuthentication() {

        // when
        boolean result = jwtTokenClaimAuthenticationProvider.supports(authentication.getClass());

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldNotSupportAnyOtherThanUsernamePasswordAuthentication() {

        // when
        boolean result = jwtTokenClaimAuthenticationProvider.supports(jwtTokenClaimAuthenticationProvider.getClass());

        // then
        assertThat(result, is(false));
    }
}
