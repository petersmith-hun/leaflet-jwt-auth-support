package hu.psprog.leaflet.jwt.auth.support.bridge;

import hu.psprog.leaflet.jwt.auth.support.mock.WithMockedJWTUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.Map;

import static hu.psprog.leaflet.jwt.auth.support.mock.MockedJWTUserSecurityContextFactory.TOKEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link JWTTokenBasedRequestAuthentication}.
 *
 * @author Peter Smith
 */
@Extensions({
        @ExtendWith(SpringExtension.class),
        @ExtendWith(MockitoExtension.class)
})
@TestExecutionListeners(listeners = {
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class JWTTokenBasedRequestAuthenticationTest {

    private static final String HEADER_PARAMETER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_VALUE = "Bearer " + TOKEN;

    @InjectMocks
    private JWTTokenBasedRequestAuthentication requestAuthentication;

    @Test
    @WithMockedJWTUser
    public void shouldReturnRequestAuthenticationHeaderMap() {

        // when
        Map<String, String> result = requestAuthentication.getAuthenticationHeader();

        // then
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(HEADER_PARAMETER_AUTHORIZATION), equalTo(AUTHORIZATION_VALUE));
    }
}
