package be.witspirit.authorizer;

import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.authorizer.io.AuthPolicy;
import be.witspirit.authorizer.io.TokenAuthorizerContext;
import be.witspirit.common.exception.InvalidTokenException;
import be.witspirit.common.test.EnvValue;
import be.witspirit.common.test.WithSysEnv;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


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

        assertThat(authPolicy.getPrincipalId()).isEqualTo("testUserId");
        assertThat(policyHelper.getAllowResources().length).isEqualTo(0);
        assertThat(policyHelper.getDenyResources().length).isEqualTo(1);

        Map<String, Object> context = authPolicy.getContext();
        assertThat(context.size()).isEqualTo(0); // For unauthorized users, we don't provide profile info
    }

    @Test
    @WithSysEnv(@EnvValue(key = "authorizedEmails", val = "testUser@example.com"))
    void validTokenAuthorized() {
        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());
        AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(authorizerContext("ValidToken"), null);

        PolicyHelper policyHelper = new PolicyHelper(authPolicy);

        assertThat(authPolicy.getPrincipalId()).isEqualTo("testUserId");
        assertThat(policyHelper.getAllowResources().length).isEqualTo(1);
        assertThat(policyHelper.getDenyResources().length).isEqualTo(0);


        Map<String, Object> context = authPolicy.getContext();
        assertThat(context.size()).isEqualTo(2); // For unauthorized users, we don't provide profile info

        assertThat(context.get("name")).isEqualTo("Test User");
        assertThat(context.get("email")).isEqualTo("testUser@example.com");
    }

    @Test
    void invalidToken() {
        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> apiGwAuthorizerHandler.handleRequest(authorizerContext("InvalidToken"), null));
        assertThat(exception.getMessage()).contains("Unauthorized");
    }

    @Test
    @WithSysEnv(@EnvValue(key = "authorizedEmails", val = "testUser@example.com"))
    void allowBearerPrefix() {
        // In the OAuth protocol, the token is prefixed with Bearer before the actual token. The Amazon Login service
        // however does not allow this. But for easier integration with clients, I am going to detect and strip this from
        // the token, to ensure it works irrespective.
        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());
        AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(authorizerContext("Bearer ValidToken"), null);

        assertThat(authPolicy.getPrincipalId()).isEqualTo("testUserId");
    }

    @Test
    void specialCaseCoverageBooster() {
        ApiGwAuthorizerHandler apiGwAuthorizerHandler = new ApiGwAuthorizerHandler(new TestProfileService());

        // Null Token case
        assertThrows(RuntimeException.class, () -> apiGwAuthorizerHandler.handleRequest(authorizerContext(null), null));

        // Weird size Token case
        assertThrows(RuntimeException.class, () -> apiGwAuthorizerHandler.handleRequest(authorizerContext("short"), null));

        // Alternate ARN size
        TokenAuthorizerContext tokenContext = authorizerContext("ValidToken");
        tokenContext.setMethodArn("arn:aws:execute-api:test-region:my-account:api-id/api-stage/method");
        AuthPolicy authPolicy = apiGwAuthorizerHandler.handleRequest(tokenContext, null);
        assertThat(authPolicy.getPrincipalId()).isEqualTo("testUserId");
    }


    private TokenAuthorizerContext authorizerContext(String token) {
        TokenAuthorizerContext authorizerContext = new TokenAuthorizerContext();
        authorizerContext.setType("TOKEN");
        authorizerContext.setMethodArn("arn:aws:execute-api:test-region:my-account:api-id/api-stage/method/resource");
        authorizerContext.setAuthorizationToken(token);
        return authorizerContext;
    }

    private static class PolicyHelper {
        private Map<String, Object> allowStatement = null;
        private Map<String, Object> denyStatement = null;

        PolicyHelper(AuthPolicy authPolicy) {
            Map<String, Object>[] statements = (Map<String, Object>[]) authPolicy.getPolicyDocument().get("Statement");
            assertThat(statements.length).isEqualTo(2);

            for (Map<String, Object> statement : statements) {
                String effect = (String) statement.get("Effect");
                if (effect.equals("Allow")) {
                    allowStatement = statement;
                } else if (effect.equals("Deny")) {
                    denyStatement = statement;
                } else {
                    fail("Unexpected statement effect: " + effect);
                }
            }

            assertNotNull(allowStatement, "Missing Allow statement");
            assertNotNull(denyStatement, "Missing Deny statement");
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
