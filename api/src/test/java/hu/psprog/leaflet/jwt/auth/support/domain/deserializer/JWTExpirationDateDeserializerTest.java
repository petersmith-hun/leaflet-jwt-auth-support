package hu.psprog.leaflet.jwt.auth.support.domain.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import hu.psprog.leaflet.jwt.auth.support.AbstractTokenRelatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link JWTExpirationDateDeserializer}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class JWTExpirationDateDeserializerTest extends AbstractTokenRelatedTest {

    private static final String TIMESTAMP = "1515320501";
    private static final String EXPECTED_DATE = "2018-01-07 10:21:41+0000";

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @InjectMocks
    private JWTExpirationDateDeserializer jwtExpirationDateDeserializer;

    @Test
    public void shouldDeserialize() throws IOException, ParseException {

        // given
        given(jsonParser.getText()).willReturn(TIMESTAMP);

        // when
        Date result = jwtExpirationDateDeserializer.deserialize(jsonParser, deserializationContext);

        // then
        assertThat(result, equalTo(DATE_FORMAT.parse(EXPECTED_DATE)));
    }
}