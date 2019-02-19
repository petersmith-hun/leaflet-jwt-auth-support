package hu.psprog.leaflet.jwt.auth.support;

import hu.psprog.leaflet.jwt.auth.support.domain.AuthenticationUserDetailsModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Test base for token related tests.
 *
 * @author Peter Smith
 */
public class AbstractTokenRelatedTest {

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    protected static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1aWQiOjAsInVzciI6InN2Yy1sZWFm" +
            "bGV0LXRlc3QiLCJuYW1lIjoibGVhZmxldC10ZXN0IiwiZXhwIjoxN" +
            "TE1MzI3MTIxLCJpYXQiOjE1MTUzMjM1MjEsInJvbCI6IlNFUlZJQ0" +
            "UifQ.fake-token-signature";
    protected static final String EXPECTED_ROLE = "SERVICE";
    protected static final GrantedAuthority SERVICE_ROLE = new SimpleGrantedAuthority(EXPECTED_ROLE);
    protected static final String EXPECTED_NAME = "leaflet-test";
    private static final long EXPECTED_USER_ID = 0L;

    protected AuthenticationUserDetailsModel prepareAuthenticationUserDetailsModel(String expectedDate) throws ParseException {
        return AuthenticationUserDetailsModel.getBuilder()
                .withName(EXPECTED_NAME)
                .withRole(EXPECTED_ROLE)
                .withId(EXPECTED_USER_ID)
                .withExpiration(DATE_FORMAT.parse(expectedDate))
                .build();
    }
}
