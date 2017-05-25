package be.witspirit.amazonlogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
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

            Response response = Request.Get(profileUrl)
                    .addHeader("Authorization", "bearer " + accessToken)
                    .execute();
            HttpResponse httpResponse = response.returnResponse();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(httpResponse.getEntity());
            if (statusCode == 200) {
                LOG.info("Received profile: {}", body);

                AmazonProfile amazonProfile = new ObjectMapper().readValue(body, AmazonProfile.class);
                return amazonProfile;
            } else if (statusCode == 400) {
                // Expect this to map to InvalidToken, but not entirely sure
                LOG.info("Received 400 : {}", body);
                throw new InvalidTokenException();
            } else {
                LOG.error("Unexpected response from Login with Amazon : {} - {}", httpResponse.getStatusLine().toString(), body);
                throw new RuntimeException("Unexpected response from Login with Amazon, check the logs for details");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain profile from Login with Amazon", e);
        }
    }
}
