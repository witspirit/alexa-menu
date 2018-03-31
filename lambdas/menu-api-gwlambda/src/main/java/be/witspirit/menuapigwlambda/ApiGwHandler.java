package be.witspirit.menuapigwlambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Slightly higher level abstraction of the RequestStreamHandler for API Gateway requests
 */
public interface ApiGwHandler {

    APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request);

}
