package be.witspirit.menuapigwlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MenuApiHandler implements RequestStreamHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MenuApiHandler.class);

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LOG.debug("Handle Request - BEGIN");

        String lambdaInput = IOUtils.toString(input);
        LOG.debug(lambdaInput);

        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Apparently the event object does not contain all actual properties !

        APIGatewayProxyRequestEvent apiGwRequest = jsonMapper.readValue(lambdaInput, APIGatewayProxyRequestEvent.class);
        LOG.info("API GW Request : "+ apiGwRequest.getHttpMethod() + " " + apiGwRequest.getPath());

        APIGatewayProxyResponseEvent apiGwResponse = new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello World");
        jsonMapper.writeValue(output, apiGwResponse);

        LOG.debug("Handle Request - END");
    }
}
