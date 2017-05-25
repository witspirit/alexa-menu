package be.witspirit.amazonlogin;

import be.witspirit.amazonlogin.support.TestResources;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Try to verify the behavior of the AmazonProfileService
 */
public class AmazonProfileServiceTest {

    @Test
    void defaultStartup() {
        // Just to check that we didn't break the default bootstrap
        new AmazonProfileService();
    }

    @Test
    void successProfile() {
        // They also have a Rule, but this is not compatible with JUnit 5 - So have to setup manually for now
        withWireMock(() -> {

            stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer ValidToken"))
                    .willReturn(aResponse().withStatus(200).withBody(TestResources.classpath("/profiles/testprofile.json"))));

            AmazonProfileService profileService = new AmazonProfileService("http://localhost:8089/user/profile");
            AmazonProfile profile = profileService.getProfile("ValidToken");

            assertNotNull(profile);
            assertThat(profile.getName(), is("Test User"));
            assertThat(profile.getEmail(), is("test.user@example.com"));
            assertThat(profile.getUserId(), is("amzn1.account.TESTACCOUNTID"));

        });
    }

    @Test
    void invalidToken() {
        withWireMock(() -> {
            stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer InvalidToken"))
                    .willReturn(aResponse().withStatus(400)));

            AmazonProfileService profileService = new AmazonProfileService("http://localhost:8089/user/profile");
            // There is probably a nicer way in JUnit 5, but haven't checked yet and wonder if it is compatible with this lambda wrapper
            try {
                AmazonProfile profile = profileService.getProfile("InvalidToken");
                fail("Expected InvalidTokenException, but service continued and returned "+profile);
            } catch (InvalidTokenException e) {
                // Expected exception
            }
        });
    }

    @Test
    void unexpectedResponse() {
        withWireMock(() -> {
            stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer broken"))
                    .willReturn(aResponse().withStatus(401)));

            AmazonProfileService profileService = new AmazonProfileService("http://localhost:8089/user/profile");
            // There is probably a nicer way in JUnit 5, but haven't checked yet and wonder if it is compatible with this lambda wrapper
            try {
                AmazonProfile profile = profileService.getProfile("broken");
                fail("Expected RuntimeException, but service continued and returned "+profile);
            } catch (RuntimeException e) {
                // Expected exception
                if (e instanceof InvalidTokenException) {
                    fail("Expected plain RuntimeException, but got InvalidTokenException");
                }
                assertThat(e.getMessage(), containsString("Unexpected response"));
            }
        });
    }

    @Test
    void ioProblem() {
        withWireMock(() -> {
            stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer ioProblem"))
                    .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));

            AmazonProfileService profileService = new AmazonProfileService("http://localhost:8089/user/profile");
            // There is probably a nicer way in JUnit 5, but haven't checked yet and wonder if it is compatible with this lambda wrapper
            try {
                AmazonProfile profile = profileService.getProfile("ioProblem");
                fail("Expected RuntimeException, but service continued and returned "+profile);
            } catch (RuntimeException e) {
                // Expected exception
                if (e instanceof InvalidTokenException) {
                    fail("Expected plain RuntimeException, but got InvalidTokenException");
                }
                assertThat(e.getMessage(), containsString("Failed"));
            }
        });
    }

    private void withWireMock(Runnable runnable) {
        WireMockServer mockServer = new WireMockServer(wireMockConfig().port(8089));
        try {
            mockServer.start();
            WireMock.configureFor(8089);

            runnable.run();

        } finally {
            mockServer.stop();
        }
    }

}
