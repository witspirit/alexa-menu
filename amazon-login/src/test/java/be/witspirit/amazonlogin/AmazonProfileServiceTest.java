package be.witspirit.amazonlogin;

import be.witspirit.common.exception.InvalidTokenException;
import be.witspirit.common.test.TestResources;
import be.witspirit.common.test.WithWireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.Test;

import static be.witspirit.common.test.WireMockExtension.SERVER_URL;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Try to verify the behavior of the AmazonProfileService
 */
@WithWireMock
public class AmazonProfileServiceTest {

    @Test
    void defaultStartup() {
        // Just to check that we didn't break the default bootstrap
        new AmazonProfileService();
    }

    @Test
    void successProfile() {

        stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer ValidToken"))
                .willReturn(aResponse().withStatus(200).withBody(TestResources.classpath("/profiles/testprofile.json"))));

        AmazonProfileService profileService = new AmazonProfileService(SERVER_URL + "/user/profile");
        AmazonProfile profile = profileService.getProfile("ValidToken");

        assertNotNull(profile);
        assertThat(profile.getName()).isEqualTo("Test User");
        assertThat(profile.getEmail()).isEqualTo("test.user@example.com");
        assertThat(profile.getUserId()).isEqualTo("amzn1.account.TESTACCOUNTID");
        assertThat(profile.toString()).isEqualTo("amzn1.account.TESTACCOUNTID (Test User - test.user@example.com)");

    }

    @Test
    void invalidToken() {
        stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer InvalidToken"))
                .willReturn(aResponse().withStatus(400)));

        AmazonProfileService profileService = new AmazonProfileService(SERVER_URL + "/user/profile");
        assertThrows(InvalidTokenException.class, () -> profileService.getProfile("InvalidToken"));
    }

    @Test
    void unexpectedResponse() {
        stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer broken"))
                .willReturn(aResponse().withStatus(401)));

        AmazonProfileService profileService = new AmazonProfileService(SERVER_URL + "/user/profile");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> profileService.getProfile("broken"));
        assertThat(exception.getMessage()).contains("Unexpected response");
    }

    @Test
    void ioProblem() {
        stubFor(get(urlEqualTo("/user/profile")).withHeader("Authorization", equalTo("bearer ioProblem"))
                .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));

        AmazonProfileService profileService = new AmazonProfileService(SERVER_URL + "/user/profile");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> profileService.getProfile("ioProblem"));
        assertThat(exception.getMessage()).contains("Failed");
    }

}
