package hu.psprog.leaflet.jwt.auth.support.service.impl;

import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.jwt.auth.support.AbstractTokenRelatedTest;
import hu.psprog.leaflet.jwt.auth.support.exception.TokenAuthenticationFailureException;
import hu.psprog.leaflet.jwt.auth.support.service.impl.utility.AuthenticationUtility;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link JWTAuthenticationServiceImpl}.
 *
 * @author Peter Smith
 */
public class JWTAuthenticationServiceImplTest extends AbstractTokenRelatedTest {

    private static final String TOKEN = "auth-token";
    private static final String USERNAME = "test-user";
    private static final String CREDENTIALS = "credentials";

    @Mock
    private UserBridgeService userBridgeService;

    @Mock
    private AuthenticationUtility authenticationUtility;

    @Mock
    private Authentication baseAuthentication;

    @Mock
    private Authentication jwtAuthentication;

    @InjectMocks
    private JWTAuthenticationServiceImpl authenticationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(baseAuthentication.getPrincipal()).willReturn(USERNAME);
        given(baseAuthentication.getCredentials()).willReturn(CREDENTIALS);
    }

    @Test
    public void shouldDemandPasswordReset() throws CommunicationFailureException {

        // given
        PasswordResetDemandRequestModel passwordResetDemandRequestModel = new PasswordResetDemandRequestModel();

        // when
        authenticationService.demandPasswordReset(passwordResetDemandRequestModel);

        // then
        verify(userBridgeService).demandPasswordReset(passwordResetDemandRequestModel, null);
    }

    @Test
    public void shouldConfirmPasswordReset() throws CommunicationFailureException {

        // given
        UserPasswordRequestModel userPasswordRequestModel = new UserPasswordRequestModel();

        // when
        authenticationService.confirmPasswordReset(userPasswordRequestModel, TOKEN);

        // then
        verify(authenticationUtility).createAndStoreTemporal(TOKEN);
        verify(userBridgeService).confirmPasswordReset(userPasswordRequestModel, null);
    }

    @Test
    public void shouldRenewToken() throws CommunicationFailureException {

        // given
        given(userBridgeService.renewToken()).willReturn(prepareLoginResponseDataModel(true));

        // when
        authenticationService.renewToken(baseAuthentication);

        // then
        verify(authenticationUtility).replace(USERNAME, TOKEN);
    }

    @Test
    public void shouldRevokeToken() throws CommunicationFailureException {

        // when
        authenticationService.revokeToken();

        // then
        verify(userBridgeService).revokeToken();
    }

    @Test
    public void shouldClaimTokenWithSuccess() throws CommunicationFailureException, ParseException {

        // given
        LoginResponseDataModel loginResponseDataModel = prepareLoginResponseDataModel(true);
        given(userBridgeService.claimToken(prepareLoginRequestModel())).willReturn(loginResponseDataModel);
        given(authenticationUtility.create(baseAuthentication, loginResponseDataModel)).willReturn(jwtAuthentication);

        // when
        Authentication result = authenticationService.claimToken(baseAuthentication);

        // then
        assertThat(result, equalTo(jwtAuthentication));
    }

    @Test(expected = TokenAuthenticationFailureException.class)
    public void shouldThrowTokenAuthenticationFailureOnInvalidCredentials() throws CommunicationFailureException {

        // given
        given(userBridgeService.claimToken(prepareLoginRequestModel())).willReturn(prepareLoginResponseDataModel(false));

        // when
        authenticationService.claimToken(baseAuthentication);

        // then
        // exception expected
    }

    @Test(expected = TokenAuthenticationFailureException.class)
    public void shouldThrowTokenAuthenticationFailureOnNullResponse() throws CommunicationFailureException {

        // given
        given(userBridgeService.claimToken(prepareLoginRequestModel())).willReturn(null);

        // when
        authenticationService.claimToken(baseAuthentication);

        // then
        // exception expected
    }

    private LoginRequestModel prepareLoginRequestModel() {

        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(USERNAME);
        loginRequestModel.setPassword(CREDENTIALS);

        return loginRequestModel;
    }

    private LoginResponseDataModel prepareLoginResponseDataModel(boolean withSuccess) {

        return LoginResponseDataModel.getBuilder()
                .withToken(TOKEN)
                .withStatus(withSuccess
                        ? LoginResponseDataModel.AuthenticationResult.AUTH_SUCCESS
                        : LoginResponseDataModel.AuthenticationResult.INVALID_CREDENTIALS)
                .build();
    }
}
