package hu.psprog.leaflet.jwt.auth.support.filter;

import hu.psprog.leaflet.jwt.auth.support.logout.ForcedLogoutHandler;
import hu.psprog.leaflet.jwt.auth.support.mock.WithMockedJWTUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link ExpiredSessionFilter}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {
        DirtiesContextTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class ExpiredSessionFilterTest {

    @Mock
    private FilterChain filterChain;

    @Mock
    private ForcedLogoutHandler forcedLogoutHandler;

    @InjectMocks
    private ExpiredSessionFilter expiredSessionFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @WithMockedJWTUser
    public void shouldClearSessionOnUnauthorizedResponse() throws ServletException, IOException {

        //given
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // when
        expiredSessionFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(forcedLogoutHandler).forceLogout(request);
    }

    @Test
    @WithMockedJWTUser
    public void shouldNotClearSessionOnHTTP200Response() throws ServletException, IOException {

        //given
        response.setStatus(HttpServletResponse.SC_OK);

        // when
        expiredSessionFilter.doFilterInternal(request, response, filterChain);

        // then
        verifyZeroInteractions(forcedLogoutHandler);
    }

    @Test
    @WithMockUser
    public void shouldNotClearSessionWhenUserIsNotAuthenticatedByJWT() throws ServletException, IOException {

        //given
        response.setStatus(HttpServletResponse.SC_OK);

        // when
        expiredSessionFilter.doFilterInternal(request, response, filterChain);

        // then
        verifyZeroInteractions(forcedLogoutHandler);
    }

    @Test
    @WithMockedJWTUser(authenticated = false)
    public void shouldNotClearSessionWhenUserIsMarkedAsNotAuthenticated() throws ServletException, IOException {

        //given
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // when
        expiredSessionFilter.doFilterInternal(request, response, filterChain);

        // then
        verifyZeroInteractions(forcedLogoutHandler);
    }
}
