package be.witspirit.amazonlogin;

import be.witspirit.amazonlogin.support.TestResources;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
        assertThat(amazonProfile.getName(), is("Test User"));
        assertThat(amazonProfile.getEmail(), is("test.user@example.com"));
        assertThat(amazonProfile.getUserId(), is("amzn1.account.TESTACCOUNTID"));
    }
}
