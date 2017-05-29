package be.witspirit.authorizer.io;

import be.witspirit.common.test.TestResources;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Although the AuthPolicy class was taken from the Blueprint, still writing a test to better
 * understand its usage and to have a clean coverage statistic.
 */
public class AuthPolicyTest {

    private static ObjectMapper mapper;

    @BeforeAll
    static void initMapper() {
        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    void constructorAllowAll() throws JsonProcessingException, JSONException {
        AuthPolicy authPolicy = new AuthPolicy("testPrincipalId",
                AuthPolicy.PolicyDocument.getAllowAllPolicy(
                        "testRegion",
                        "testAccountId",
                        "testRestApiId",
                        "testStage"));

        assertEquals("allow_all", authPolicy);
    }

    @Test
    void settersDenyAll() {
        AuthPolicy authPolicy = new AuthPolicy();
        authPolicy.setPrincipalId("testPrincipalId");
        authPolicy.setPolicyDocument(AuthPolicy.PolicyDocument.getDenyAllPolicy(
                "testRegion",
                "estAccountId",
                "testRestApiId",
                "testStage"
        ));

        assertEquals("deny_all", authPolicy);
    }

    @Test
    void allowOneWithContext() {
        AuthPolicy.PolicyDocument allowOneDoc = AuthPolicy.PolicyDocument.getAllowOnePolicy(
                "testRegion",
                "testAccountId",
                "testRestApiId",
                "testStage",
                AuthPolicy.HttpMethod.GET,
                "testResourcePath");

        AuthPolicy policy = new AuthPolicy("testPrincipalId", allowOneDoc);
        policy.getContext().put("testContextKey", "testContextValue");

        assertEquals("allow_one_with_context", policy);
    }

    @Test
    void denyOneWithSomeArtificialCallsForCoverage() {
        AuthPolicy.PolicyDocument denyOnePolicy = AuthPolicy.PolicyDocument.getDenyOnePolicy(
                "testRegion",
                "testAwsAccountId",
                "testRestApiId",
                "testStage",
                AuthPolicy.HttpMethod.HEAD,
                "/");

        String version = denyOnePolicy.getVersion();
        Assertions.assertEquals("2012-10-17", version);
        denyOnePolicy.setVersion(version);

        String effect = denyOnePolicy.getStatement()[1].getEffect();
        Assertions.assertEquals("Deny", effect);
        String action = denyOnePolicy.getStatement()[1].getAction();
        Assertions.assertEquals("execute-api:Invoke", action);

        AuthPolicy.Statement statement = new AuthPolicy.Statement("Allow", "dummyAction", new ArrayList<String>(), new HashMap<String, Map<String, Object>>());
        statement.addCondition("OP", "testConditionKey", "testConditionValue");

        denyOnePolicy.addStatement(statement);

        denyOnePolicy.allowMethod(AuthPolicy.HttpMethod.DELETE, "/some/dummy/resource");

        assertEquals("artificial_policy", new AuthPolicy("testPrincipalId", denyOnePolicy));

    }

    private void assertEquals(String expectedPolicyName, AuthPolicy policy)  {
        try {
            String authPolicyJson = mapper.writeValueAsString(policy);
            System.out.println(authPolicyJson);

            String expectedPolicy = TestResources.classpath("/policies/" + expectedPolicyName + ".json");
            JSONAssert.assertEquals(expectedPolicy, authPolicyJson, JSONCompareMode.NON_EXTENSIBLE);
        } catch (JSONException | JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize or compare the JSON", e);
        }
    }
}
