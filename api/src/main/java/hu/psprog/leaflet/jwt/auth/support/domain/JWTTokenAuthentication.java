package hu.psprog.leaflet.jwt.auth.support.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Standard Spring Security {@link Authentication} implementation for JWT token based authentication.
 *
 * @author Peter Smith
 */
public class JWTTokenAuthentication implements Authentication {

    private String emailAddress;
    private String token;
    private boolean authenticated = true;
    private AuthenticationUserDetailsModel details;

    private JWTTokenAuthentication() {
        // prevent direct instantiation
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(details)
                .map(AuthenticationUserDetailsModel::getRole)
                .map(AuthorityUtils::createAuthorityList)
                .orElse(Collections.emptyList());
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return emailAddress;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return details.getName();
    }

    public static JWTTokenAuthenticationBuilder getBuilder() {
        return new JWTTokenAuthenticationBuilder();
    }

    /**
     * Builder for {@link JWTTokenAuthentication}.
     */
    public static class JWTTokenAuthenticationBuilder {

        private JWTTokenAuthentication jwtTokenAuthentication;

        private JWTTokenAuthenticationBuilder() {
            jwtTokenAuthentication = new JWTTokenAuthentication();
        }

        public JWTTokenAuthenticationBuilder withEmailAddress(String emailAddress) {
            jwtTokenAuthentication.emailAddress = emailAddress;
            return this;
        }

        public JWTTokenAuthenticationBuilder withToken(String token) {
            jwtTokenAuthentication.token = token;
            return this;
        }

        public JWTTokenAuthenticationBuilder withAuthenticated(boolean authenticated) {
            jwtTokenAuthentication.authenticated = authenticated;
            return this;
        }

        public JWTTokenAuthenticationBuilder withDetails(AuthenticationUserDetailsModel details) {
            jwtTokenAuthentication.details = details;
            return this;
        }

        public JWTTokenAuthentication build() {
            return jwtTokenAuthentication;
        }
    }
}
