package hu.psprog.leaflet.jwt.auth.support.logout;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * Helper component to handle forced-logout.
 * Can be used to trigger "local" logout in case the backend has already invalidated the user's token.
 *
 * @author Peter Smith
 */
@Component
public class ForcedLogoutHandler {

    /**
     * Forces logout by clearing security context, destroying the active session and all the cookies assigned to it.
     *
     * @param request {@link HttpServletRequest} object to access session and cookie data
     */
    public void forceLogout(HttpServletRequest request) {

        SecurityContextHolder.clearContext();
        request.getSession(false).invalidate();
        Stream.of(request.getCookies())
                .forEach(cookie -> cookie.setMaxAge(0));
    }
}
