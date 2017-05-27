package be.witspirit.authorizer;

import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.InvalidTokenException;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.authorizer.io.AuthPolicy;
import be.witspirit.authorizer.io.TokenAuthorizerContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Illustrates the behavior of ApiGwAuthorizerHandler
 */
public class ApiGwAuthorizerHandlerTest {

    @Test
    void bootstrap() {
        new ApiGwAuthorizerHandler();
    }

    @Test
    void validTokenUnauthorized() {
        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());
        AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(authorizerContext("ValidToken"), null);

        PolicyHelper policyHelper = new PolicyHelper(authPolicy);

        assertThat(authPolicy.getPrincipalId(), is("testUserId"));
        assertThat(policyHelper.getAllowResources().length, is(0));
        assertThat(policyHelper.getDenyResources().length, is(1));

        Map<String, Object> context = authPolicy.getContext();
        assertThat(context.size(), is(0)); // For unauthorized users, we don't provide profile info
    }

    @Test
    void validTokenAuthorized() {
        System.setProperty("authorizedEmails", "testUser@example.com");

        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());
        AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(authorizerContext("ValidToken"), null);

        PolicyHelper policyHelper = new PolicyHelper(authPolicy);

        assertThat(authPolicy.getPrincipalId(), is("testUserId"));
        assertThat(policyHelper.getAllowResources().length, is(1));
        assertThat(policyHelper.getDenyResources().length, is(0));


        Map<String, Object> context = authPolicy.getContext();
        assertThat(context.size(), is(2)); // For unauthorized users, we don't provide profile info

        assertThat(context.get("name"), is("Test User"));
        assertThat(context.get("email"), is("testUser@example.com"));
    }

    @Test
    void invalidToken() {
        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());

        try {
            AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(authorizerContext("InvalidToken"), null);
            fail("Expected RuntimeException, but service continued and delivered an AuthPolicy : " + authPolicy);
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("Unauthorized"));
        }
    }

    @Test
    void allowBearerPrefix() {
        // In the OAuth protocol, the token is prefixed with Bearer before the actual token. The Amazon Login service
        // however does not allow this. But for easier integration with clients, I am going to detect and strip this from
        // the token, to ensure it works irrespective.
        System.setProperty("authorizedEmails", "testUser@example.com");

        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());
        AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(authorizerContext("Bearer ValidToken"), null);

        assertThat(authPolicy.getPrincipalId(), is("testUserId"));
    }



    private TokenAuthorizerContext authorizerContext(String token) {
        TokenAuthorizerContext authorizerContext = new TokenAuthorizerContext();
        authorizerContext.setType("TOKEN");
        authorizerContext.setMethodArn("arn:aws:execute-api:test-region:my-account:api-id/api-stage/method/resource/resourceParam");
        authorizerContext.setAuthorizationToken(token);
        return authorizerContext;
    }

    private static class PolicyHelper {
        private Map<String, Object> allowStatement;
        private Map<String, Object> denyStatement;

        PolicyHelper(AuthPolicy authPolicy) {
            Map<String, Object>[] statements = (Map<String, Object>[]) authPolicy.getPolicyDocument().get("Statement");
            assertThat(statements.length, is(2));

            // Hoping that the sequence is stable

            allowStatement = statements[0];
            String effect = (String) allowStatement.get("Effect");
            assertThat(effect, is( "Allow"));

            denyStatement = statements[1];
            effect = (String) denyStatement.get("Effect");
            assertThat(effect, is( "Deny"));
        }

        String[] getAllowResources() {
            return (String[]) allowStatement.get("Resource");
        }

        String[] getDenyResources() {
            return (String[]) denyStatement.get("Resource");
        }


    }

    private static class TestProfileService implements ProfileService {

        @Override
        public AmazonProfile getProfile(String accessToken) {
            if (accessToken != null) {
                if (accessToken.equals("ValidToken")) {
                    return new AmazonProfile().setUserId("testUserId").setName("Test User").setEmail("testUser@example.com");
                } else if (accessToken.equals("InvalidToken")) {
                    throw new InvalidTokenException();
                }
            }
            throw new RuntimeException("Unhandled input");
        }
    }
}
