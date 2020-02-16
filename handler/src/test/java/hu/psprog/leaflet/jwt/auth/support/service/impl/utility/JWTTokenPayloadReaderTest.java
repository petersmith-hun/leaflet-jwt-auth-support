package hu.psprog.leaflet.jwt.auth.support.service.impl.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.jwt.auth.support.AbstractTokenRelatedTest;
import hu.psprog.leaflet.jwt.auth.support.domain.AuthenticationUserDetailsModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link JWTTokenPayloadReader}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class JWTTokenPayloadReaderTest extends AbstractTokenRelatedTest {

    private static final String EXPECTED_DATE = "2018-01-07 12:12:01+0000";

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private JWTTokenPayloadReader jwtTokenPayloadReader;

    @Test
    public void shouldReadPayload() throws ParseException {

        // when
        AuthenticationUserDetailsModel result = jwtTokenPayloadReader.readPayload(TOKEN);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(prepareAuthenticationUserDetailsModel(EXPECTED_DATE)));
    }

    @Test
    public void shouldReturnEmptyResponseOnException() throws IOException {

        // given
        doThrow(JsonProcessingException.class).when(objectMapper).readValue(anyString(), eq(AuthenticationUserDetailsModel.class));

        // when
        AuthenticationUserDetailsModel result = jwtTokenPayloadReader.readPayload(TOKEN);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(AuthenticationUserDetailsModel.getBuilder().build()));
    }
}
