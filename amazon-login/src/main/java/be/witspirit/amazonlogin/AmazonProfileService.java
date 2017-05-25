package be.witspirit.amazonlogin;

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

    public static final String AWS_PROFILE_URL = "https://api.amazon.com/user/profile";

    private final String profileUrl;

    public AmazonProfileService() {
        this(AWS_PROFILE_URL);
    }

    public AmazonProfileService(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public AmazonProfile getProfile(String accessToken) {
        try {

            Content profileResponse = Request.Get(profileUrl)
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
