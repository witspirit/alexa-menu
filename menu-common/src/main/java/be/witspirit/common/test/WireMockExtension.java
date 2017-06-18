package be.witspirit.common.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.extension.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Attempt to create a Wire Mock extension according to the JUnit 5 extension model
 */
public class WireMockExtension implements BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, TestExecutionExceptionHandler {


    public static final int PORT = 8089;
    public static final String SERVER_URL = "http://localhost:"+PORT;

    private ExtensionMode mode = ExtensionMode.OFF;
    private WireMockServer mockServer;

    private enum ExtensionMode {
        OFF,
        CLASS,
        METHOD;
    }

    private void startMockServer() {
        if (mockServer != null && mockServer.isRunning()) {
            throw new IllegalStateException("Mock Server is already running ! Probably an inconsistent setup !");
        }

        mockServer = new WireMockServer(wireMockConfig().port(PORT));
        mockServer.start();
        WireMock.configureFor(PORT);
    }

    private void stopMockServer() {
        if (mockServer != null) {
            mockServer.stop();
            mockServer = null;
        }
    }

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        WithWireMock withWireMock = context.getTestClass().get().getAnnotation(WithWireMock.class);
        if (withWireMock != null) {
            mode = ExtensionMode.CLASS;
            startMockServer();
        }
    }

    @Override
    public void afterAll(ContainerExtensionContext context) throws Exception {
        if (mode == ExtensionMode.CLASS) {
            stopMockServer();
        }
    }

    @Override
    public void beforeTestExecution(TestExtensionContext context) throws Exception {
        if (mode == ExtensionMode.CLASS) {
            return;
        }

        WithWireMock withWireMock = context.getTestMethod().get().getAnnotation(WithWireMock.class);
        if (withWireMock != null) {
            mode = ExtensionMode.METHOD;
            startMockServer();
        }
    }


    @Override
    public void afterTestExecution(TestExtensionContext context) throws Exception {
        if (mode == ExtensionMode.METHOD) {
            stopMockServer();
        }
    }

    @Override
    public void handleTestExecutionException(TestExtensionContext context, Throwable throwable) throws Throwable {
        if (mode == ExtensionMode.METHOD) {
            stopMockServer();
        }
        // Propagate
        throw throwable;
    }

}
