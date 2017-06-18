package be.witspirit.common.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExtensionContext;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Attempt to create a Wire Mock extension according to the JUnit 5 extension model
 */
public class WireMockExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestExecutionExceptionHandler {


    public static final int PORT = 8089;
    public static final String SERVER_URL = "http://localhost:"+PORT;

    private WireMockServer mockServer;

    @Override
    public void beforeTestExecution(TestExtensionContext context) throws Exception {
        mockServer = new WireMockServer(wireMockConfig().port(PORT));
        mockServer.start();
        WireMock.configureFor(PORT);
    }

    @Override
    public void afterTestExecution(TestExtensionContext context) throws Exception {
        if (mockServer != null) {
            mockServer.stop();
        }
    }

    @Override
    public void handleTestExecutionException(TestExtensionContext context, Throwable throwable) throws Throwable {
        if (mockServer != null) {
            mockServer.stop();
        }
        // Propagate
        throw throwable;
    }
}
