package hu.psprog.leaflet.jwt.auth.support.domain;

import hu.psprog.leaflet.jwt.auth.support.AbstractTokenRelatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link JWTTokenAuthentication}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class JWTTokenAuthenticationTest extends AbstractTokenRelatedTest {

    private static final String EXPECTED_DATE = "2018-01-07 12:12:01+0000";
    private static final String EMAIL_ADDRESS = "test@local.dev";

    @Test
    public void shouldBuildAuthentication() throws ParseException {

        // given
        AuthenticationUserDetailsModel details = prepareAuthenticationUserDetailsModel(EXPECTED_DATE);
        JWTTokenAuthentication.JWTTokenAuthenticationBuilder builder = JWTTokenAuthentication.getBuilder()
                .withAuthenticated(true)
                .withDetails(details)
                .withEmailAddress(EMAIL_ADDRESS)
                .withToken(TOKEN);

        // when
        JWTTokenAuthentication result = builder.build();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getAuthorities().size(), equalTo(1));
        assertThat(result.getAuthorities().contains(SERVICE_ROLE), is(true));
        assertThat(result.getCredentials(), equalTo(TOKEN));
        assertThat(result.getDetails(), equalTo(details));
        assertThat(result.getPrincipal(), equalTo(EMAIL_ADDRESS));
        assertThat(result.getName(), equalTo(EXPECTED_NAME));
        assertThat(result.isAuthenticated(), is(true));
    }
}
