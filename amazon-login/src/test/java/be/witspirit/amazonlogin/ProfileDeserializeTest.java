package be.witspirit.amazonlogin;

import be.witspirit.common.test.TestResources;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple unit test to verify deserialization to AmazonProfile
 */
public class ProfileDeserializeTest {

    @Test
    void testProfile() throws IOException {
        String profileJson = TestResources.classpath("/profiles/testprofile.json");

        AmazonProfile amazonProfile = new ObjectMapper().readValue(profileJson, AmazonProfile.class);

        assertNotNull(amazonProfile);
        assertThat(amazonProfile.getName()).isEqualTo("Test User");
        assertThat(amazonProfile.getEmail()).isEqualTo("test.user@example.com");
        assertThat(amazonProfile.getUserId()).isEqualTo("amzn1.account.TESTACCOUNTID");
    }
}
