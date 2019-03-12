package hu.psprog.leaflet.jwt.auth.support.service.impl.utility;

import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.jwt.auth.support.AbstractTokenRelatedTest;
import hu.psprog.leaflet.jwt.auth.support.domain.AuthenticationUserDetailsModel;
import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link AuthenticationUtility}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class AuthenticationUtilityTest extends AbstractTokenRelatedTest {

    private static final String EXPECTED_DATE = "2018-01-07 12:12:01+0000";
    private static final String BASE_AUTHENTICATION_PRINCIPAL = "test@dev.local";
    private static final AuthenticationUserDetailsModel AUTHENTICATION_USER_DETAILS_MODEL = AuthenticationUserDetailsModel.getBuilder().withName("username").build();

    @Mock
    private Authentication baseAuthentication;

    @Mock
    private JWTTokenPayloadReader jwtTokenPayloadReader;

    @InjectMocks
    private AuthenticationUtility authenticationUtility;

    @Before
    public void setup() throws ParseException {
        MockitoAnnotations.initMocks(this);
        given(jwtTokenPayloadReader.readPayload(TOKEN)).willReturn(prepareAuthenticationUserDetailsModel(EXPECTED_DATE));
    }

    @Test
    public void shouldCreateAuthentication() {

        // given
        LoginResponseDataModel loginResponseDataModel = prepareLoginResponseDataModel(true);
        given(baseAuthentication.getPrincipal()).willReturn(BASE_AUTHENTICATION_PRINCIPAL);
        given(jwtTokenPayloadReader.readPayload(TOKEN)).willReturn(AUTHENTICATION_USER_DETAILS_MODEL);

        // when
        Authentication result = authenticationUtility.create(baseAuthentication, loginResponseDataModel);

        // then
        assertThat(result, notNullValue());
        assertThat(result instanceof JWTTokenAuthentication, is(true));
        assertThat(result.getPrincipal(), equalTo(BASE_AUTHENTICATION_PRINCIPAL));
        assertThat(result.getCredentials(), equalTo(TOKEN));
        assertThat(result.getDetails(), equalTo(AUTHENTICATION_USER_DETAILS_MODEL));
    }

    @Test
    public void shouldCreateTemporalAuthentication() {

        // when
        Authentication result = authenticationUtility.createTemporal(TOKEN);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getCredentials(), equalTo(TOKEN));
    }

    @Test
    public void shouldCreateAndStoreTemporalAuthentication() {

        // when
        authenticationUtility.createAndStoreTemporal(TOKEN);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials(), equalTo(TOKEN));
    }

    @Test
    public void shouldReplaceAuthentication() {

        // given
        String username = "Replaced authentication";

        // when
        authenticationUtility.replace(username, TOKEN);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials(), equalTo(TOKEN));
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), equalTo(username));
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
