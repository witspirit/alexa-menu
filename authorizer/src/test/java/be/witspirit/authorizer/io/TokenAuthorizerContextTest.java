package be.witspirit.authorizer.io;

import be.witspirit.common.test.TestResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for TokenAuthorizerContext
 */
public class TokenAuthorizerContextTest {

    @Test
    void serialization() throws JsonProcessingException, JSONException {
        TokenAuthorizerContext tokenAuthorizerContext = new TokenAuthorizerContext("TOKEN", "dummyToken", "arn:dummy:method");
        assertEquals("TOKEN", tokenAuthorizerContext.getType());
        assertEquals("dummyToken", tokenAuthorizerContext.getAuthorizationToken());
        assertEquals("arn:dummy:method", tokenAuthorizerContext.getMethodArn());

        String json = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(tokenAuthorizerContext);
        System.out.println(json);

        String expectedJson = TestResources.classpath("/contexts/tokenauthorizercontext.json");

        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.NON_EXTENSIBLE);
    }
}
