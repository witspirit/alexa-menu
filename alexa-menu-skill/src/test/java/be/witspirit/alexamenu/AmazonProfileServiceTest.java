package be.witspirit.alexamenu;

import be.witspirit.alexamenu.support.TestResources;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        WireMockServer mockServer = new WireMockServer(wireMockConfig().port(8089));
        try {
            mockServer.start();
            WireMock.configureFor(8089);

            stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer ValidToken"))
                    .willReturn(aResponse().withStatus(200).withBody(TestResources.classpath("/profiles/testprofile.json"))));

            AmazonProfileService profileService = new AmazonProfileService("http://localhost:8089/user/profile");
            AmazonProfile profile = profileService.getProfile("ValidToken");

            assertNotNull(profile);
            assertThat(profile.getName(), is("Test User"));
            assertThat(profile.getEmail(), is("test.user@example.com"));
            assertThat(profile.getUserId(), is("amzn1.account.TESTACCOUNTID"));

        } finally {
            mockServer.stop();
        }
    }

}
