package hu.psprog.leaflet.jwt.auth.support.filter;

import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import hu.psprog.leaflet.jwt.auth.support.logout.ForcedLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that forces the user to re-login when their session expires or gets invalidated on Leaflet's side.
 * User session gets invalidated if they are authenticated, though Leaflet returns with a response of status '401 Unauthorized'.
 * In this case, the SecurityContext will get cleared, session invalidated and session cookies deleted.
 *
 * @author Peter Smith
 */
@Component
@Order
public class ExpiredSessionFilter extends OncePerRequestFilter {

    private ForcedLogoutHandler forcedLogoutHandler;

    @Autowired
    public ExpiredSessionFilter(ForcedLogoutHandler forcedLogoutHandler) {
        this.forcedLogoutHandler = forcedLogoutHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        if (isAuthenticated() && isRequestUnauthorized(response)) {
            forcedLogoutHandler.forceLogout(request);
        }
    }

    private boolean isAuthenticated() {
        return isAuthenticatedByJWT(getAuthentication()) && getAuthentication().isAuthenticated();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAuthenticatedByJWT(Authentication authentication) {
        return authentication instanceof JWTTokenAuthentication;
    }

    private boolean isRequestUnauthorized(HttpServletResponse response) {
        return response.getStatus() == HttpStatus.UNAUTHORIZED.value();
    }
}
