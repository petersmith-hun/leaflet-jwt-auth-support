package hu.psprog.leaflet.jwt.auth.support.service.impl.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.jwt.auth.support.domain.AuthenticationUserDetailsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

/**
 * Extracts payload from a JWT token.
 *
 * @author Peter Smith
 */
@Component
public class JWTTokenPayloadReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenPayloadReader.class);

    private static final String TOKEN_PAYLOAD_SPLIT_REGEX_PATTERN = "\\.";
    private static final AuthenticationUserDetailsModel EMPTY_AUTHENTICATION_USER_DETAILS_MODEL = AuthenticationUserDetailsModel.getBuilder().build();

    private ObjectMapper objectMapper;

    @Autowired
    public JWTTokenPayloadReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Extracts and parses JWT payload.
     *
     * @param token JWT token
     * @return JWT payload wrapped as {@link AuthenticationUserDetailsModel} object.
     */
    AuthenticationUserDetailsModel readPayload(String token) {

        String[] tokenParts = token.split(TOKEN_PAYLOAD_SPLIT_REGEX_PATTERN);
        String payload = new String(Base64.getDecoder().decode(tokenParts[1]));

        try {
            return objectMapper.readValue(payload, AuthenticationUserDetailsModel.class);
        } catch (IOException e) {
            LOGGER.error("Could not read JWT payload", e);
            return EMPTY_AUTHENTICATION_USER_DETAILS_MODEL;
        }
    }
}
