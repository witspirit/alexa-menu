package be.witspirit.alexamenu.menustore;

import be.witspirit.common.exception.InvalidTokenException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Function;

/**
 * Client for API Gateway Menu Store
 */
public class ApiGwMenuDao implements MenuDao {
    private static final Logger LOG = LoggerFactory.getLogger(ApiGwMenuDao.class);

    private static final String DEFAULT_BASE_URL = "https://4abokujlye.execute-api.eu-west-1.amazonaws.com/prod";

    private final String baseUrl;

    private String accessToken = null;

    public ApiGwMenuDao() {
        this(DEFAULT_BASE_URL);
    }

    public ApiGwMenuDao(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ApiGwMenuDao withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * Fetches the menu for a given date, format yyyyMMdd
     * @param dateKey Date in yyyyMMdd format
     * @return The menu for the given date
     */
    @Override
    public Menu getMenu(String dateKey) {
        return invoke(Request.Get(baseUrl + "/menus/" + dateKey), this::parseAsMenu);
    }

    @Override
    public void setMenu(String dateKey, String dinner) {
        String updateJson = toMenuUpdateJson(dinner);

        invoke(Request.Put(baseUrl + "/menus/" + dateKey)
                        .bodyString(updateJson, ContentType.APPLICATION_JSON),
                this::noResponse);

    }

    private <T> T invoke(Request request, Function<String, T> resultHandler) {
        try {
            Response response = request
                    .addHeader("Authorization", accessToken)
                    .execute();
            HttpResponse httpResponse = response.returnResponse();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(httpResponse.getEntity());
            if (statusCode == 200 || statusCode == 201) {
                LOG.info("Received {} : {}", statusCode, body);

                return resultHandler.apply(body);
            } else if (statusCode == 401) {
                // Expect this to map to InvalidToken, but not entirely sure
                LOG.info("Received 401 : {}", body);
                throw new InvalidTokenException();
            } else {
                LOG.error("Unexpected response from MenuStore : {} - {}", httpResponse.getStatusLine().toString(), body);
                throw new RuntimeException("Unexpected response from MenuStore, check the logs for details");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to interact with MenuStore", e);
        }
    }

    private Void noResponse(String json) {
        // Do nothing
        return null;
    }

    private Menu parseAsMenu(String json) {
        try {
            return new ObjectMapper().readValue(json, Menu.class);
        } catch (IOException e) {
            LOG.error("Failed to parse '"+json+"' as a Menu", e);
            throw new RuntimeException("Failed to parse value as Menu, see the logs for details");
        }
    }

    private String toMenuUpdateJson(String dinner) {
        try {
            Menu updateMenu = new Menu();
            updateMenu.setMenu(dinner);
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            return mapper.writeValueAsString(updateMenu);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to create Menu Update JSON with input "+dinner, e);
            throw new RuntimeException("Failed to create Menu Update JSON with input "+dinner);
        }
    }
}
