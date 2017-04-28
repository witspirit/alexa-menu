package be.witspirit.alexamenu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Login with Amazon implementation of the profile service
 */
public class AmazonProfileService implements ProfileService {
    private static final Logger LOG = LoggerFactory.getLogger(AmazonProfileService.class);

    @Override
    public AmazonProfile getProfile(String accessToken) {
        try {

            Content profileResponse = Request.Get("https://api.amazon.com/user/profile")
                    .addHeader("Authorization", "bearer " + accessToken)
                    .execute()
                    .returnContent();

            LOG.info("Received profile: {}", profileResponse.toString());

            AmazonProfile amazonProfile = new ObjectMapper().readValue(profileResponse.toString(), AmazonProfile.class);
            return amazonProfile;

        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain profile from Login with Amazon", e);
        }
    }
}
