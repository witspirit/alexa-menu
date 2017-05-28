package be.witspirit.alexamenu;

import be.witspirit.amazonlogin.InvalidTokenException;
import com.amazon.speech.speechlet.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MenuRepository backed by API Gateway interface for the Menu Store
 */
public class ApiGwMenuRepository implements MenuRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ApiGwMenuRepository.class);

    private static final String DEFAULT_BASE_URL = "https://4abokujlye.execute-api.eu-west-1.amazonaws.com/prod";

    private final String baseUrl;

    public ApiGwMenuRepository() {
        this(DEFAULT_BASE_URL);
    }

    public ApiGwMenuRepository(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String whatIsForDinner(User user, LocalDate date) {
        String accessToken = user.getAccessToken();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateKey = dateFormat.format(date);

        try {
            Response response = Request.Get(baseUrl + "/menus/" + dateKey)
                    .addHeader("Authorization", accessToken)
                    .execute();
            HttpResponse httpResponse = response.returnResponse();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(httpResponse.getEntity());
            if (statusCode == 200) {
                LOG.info("Received menu: {}", body);

                Menu menu = new ObjectMapper().readValue(body, Menu.class);
                return menu.getMenu();
            } else if (statusCode == 401) {
                // Expect this to map to InvalidToken, but not entirely sure
                LOG.info("Received 401 : {}", body);
                throw new InvalidTokenException();
            } else {
                LOG.error("Unexpected response from MenuStore : {} - {}", httpResponse.getStatusLine().toString(), body);
                throw new RuntimeException("Unexpected response from MenuStore, check the logs for details");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to obtain menu from MenuStore", e);
        }

        // return "We haven't decided yet";;
    }
}
