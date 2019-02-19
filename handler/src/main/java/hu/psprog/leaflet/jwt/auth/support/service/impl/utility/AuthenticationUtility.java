package hu.psprog.leaflet.jwt.auth.support.service.impl.utility;

import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.jwt.auth.support.domain.JWTTokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication utilities.
 *
 * @author Peter Smith
 */
@Component
public class AuthenticationUtility {

    private JWTTokenPayloadReader jwtTokenPayloadReader;

    @Autowired
    public AuthenticationUtility(JWTTokenPayloadReader jwtTokenPayloadReader) {
        this.jwtTokenPayloadReader = jwtTokenPayloadReader;
    }

    /**
     * Creates fully populated {@link JWTTokenAuthentication} object.
     * Can be used for user sessions.
     *
     * @param baseAuthentication original {@link Authentication} object (mostly {@link UsernamePasswordAuthenticationToken}) to derive authentication parameters from
     * @param loginResponseModel response for authentication request from backend
     * @return built {@link JWTTokenAuthentication} object to be stored in security context
     */
    public Authentication create(Authentication baseAuthentication, LoginResponseDataModel loginResponseModel) {
        return JWTTokenAuthentication.getBuilder()
                .withEmailAddress(baseAuthentication.getPrincipal().toString())
                .withToken(loginResponseModel.getToken())
                .withDetails(jwtTokenPayloadReader.readPayload(loginResponseModel.getToken()))
                .build();
    }

    /**
     * Creates temporal {@link JWTTokenAuthentication} object (without username).
     * Can be used for creating temporal authentication for password reset.
     *
     * @param token token to include in {@link JWTTokenAuthentication} object
     * @return populated {@link JWTTokenAuthentication} object
     */
    public Authentication createTemporal(String token) {
        return JWTTokenAuthentication.getBuilder()
                .withDetails(jwtTokenPayloadReader.readPayload(token))
                .withToken(token)
                .build();
    }

    /**
     * Creates and immediately stores temporal {@link JWTTokenAuthentication} object (without username) in SecurityContext.
     * Can be used for creating temporal authentication for password reset.
     *
     * @param token token to include in {@link JWTTokenAuthentication} object
     */
    public void createAndStoreTemporal(String token) {
        store(createTemporal(token));
    }

    /**
     * Replaces current {@link JWTTokenAuthentication} object with the given new one.
     * Can be used for session extension.
     *
     * @param username username to include in {@link JWTTokenAuthentication} object
     * @param token token to include in {@link JWTTokenAuthentication} object
     */
    public void replace(String username, String token) {

        Authentication authentication = JWTTokenAuthentication.getBuilder()
                .withEmailAddress(username)
                .withDetails(jwtTokenPayloadReader.readPayload(token))
                .withToken(token)
                .build();
        store(authentication);
    }

    /**
     * Stores given {@link Authentication} object in SecurityContext.
     *
     * @param authentication {@link Authentication} object to store
     */
    private void store(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
