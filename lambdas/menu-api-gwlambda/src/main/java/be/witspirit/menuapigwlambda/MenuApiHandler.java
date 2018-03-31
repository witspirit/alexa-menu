package be.witspirit.menuapigwlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service("menuApiHandler")
public class MenuApiHandler implements RequestStreamHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MenuApiHandler.class);

    private final ObjectMapper jsonMapper;

    private final ApiGwHandler apiGwHandler;

    public MenuApiHandler(ObjectMapper jsonMapper, ApiGwHandler apiGwHandler) {
        this.jsonMapper = jsonMapper;
        this.apiGwHandler = apiGwHandler;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        String lambdaInput = IOUtils.toString(input);
        LOG.debug(lambdaInput);

        APIGatewayProxyRequestEvent apiGwRequest = jsonMapper.readValue(lambdaInput, APIGatewayProxyRequestEvent.class);
        LOG.info("API GW Request : {} {} - {}", apiGwRequest.getHttpMethod(), apiGwRequest.getPath(), apiGwRequest.getResource());

        APIGatewayProxyResponseEvent apiGwResponse = apiGwHandler.handle(apiGwRequest);
        jsonMapper.writeValue(output, apiGwResponse);
    }
}
