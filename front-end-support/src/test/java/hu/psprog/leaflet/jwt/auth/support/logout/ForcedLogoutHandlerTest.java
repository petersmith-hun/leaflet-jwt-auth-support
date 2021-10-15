package hu.psprog.leaflet.jwt.auth.support.logout;

import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import hu.psprog.leaflet.jwt.auth.support.mock.WithMockedJWTUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.servlet.http.Cookie;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Unit tests for {@link ForcedLogoutHandler}.
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
public class ForcedLogoutHandlerTest {

    private static final String COOKIE_NAME = "cookie-name";
    private static final String COOKIE_VALUE = "cookie-value";

    private MockHttpServletRequest request;
    private MockHttpSession session;

    @InjectMocks
    private ForcedLogoutHandler forcedLogoutHandler;

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        request.setCookies(new Cookie(COOKIE_NAME, COOKIE_VALUE));
    }

    @Test
    @WithMockedJWTUser
    public void shouldForceLogoutUser() {

        // given
        assertThat("Authentication should be prepared", SecurityContextHolder.getContext().getAuthentication() instanceof JWTTokenAuthentication, is(true));

        // when
        forcedLogoutHandler.forceLogout(request);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication(), nullValue());
        assertThat(session.isInvalid(), is(true));
        assertThat(Arrays.stream(request.getCookies()).allMatch(cookie -> cookie.getMaxAge() == 0), is(true));
    }
}
