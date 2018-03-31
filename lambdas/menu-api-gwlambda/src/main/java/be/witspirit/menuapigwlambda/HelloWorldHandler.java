package be.witspirit.menuapigwlambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldHandler implements ApiGwHandler {

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        APIGatewayProxyResponseEvent apiGwResponse = new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Hello World");
        return apiGwResponse;
    }
}
