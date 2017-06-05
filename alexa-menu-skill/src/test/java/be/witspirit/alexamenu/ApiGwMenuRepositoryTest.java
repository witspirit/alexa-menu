package be.witspirit.alexamenu;

import be.witspirit.alexamenu.menustore.MenuStoreClient;
import be.witspirit.common.exception.InvalidTokenException;
import be.witspirit.common.test.TestResources;
import com.amazon.speech.speechlet.User;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Try to verify the behavior of the ApiGwMenuRepository
 */
public class ApiGwMenuRepositoryTest {

    @Test
    void defaultStartup() {
        // Just to check that we didn't break the default bootstrap
        new ApiGwMenuRepositoryTest();
    }

    @Test
    void successMenu() {
        // They also have a Rule, but this is not compatible with JUnit 5 - So have to setup manually for now
        withWireMock(() -> {

            stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("ValidToken"))
                    .willReturn(aResponse().withStatus(200).withBody(TestResources.classpath("/menus/menu.json"))));

            ApiGwMenuRepository menuRepo = new ApiGwMenuRepository(new MenuStoreClient("http://localhost:8089"));
            LocalDate date = LocalDate.of(2017, 05, 28);
            User user = User.builder().withAccessToken("ValidToken").build();
            String menu = menuRepo.whatIsForDinner(user, date);

            assertThat(menu, is("TestMenu"));
        });
    }

    @Test
    void invalidToken() {
        withWireMock(() -> {
            stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("InvalidToken"))
                    .willReturn(aResponse().withStatus(401)));

            ApiGwMenuRepository menuRepo = new ApiGwMenuRepository(new MenuStoreClient("http://localhost:8089"));
            LocalDate date = LocalDate.of(2017, 05, 28);
            User user = User.builder().withAccessToken("InvalidToken").build();

            // There is probably a nicer way in JUnit 5, but haven't checked yet and wonder if it is compatible with this lambda wrapper
            try {
                String menu = menuRepo.whatIsForDinner(user, date);
                fail("Expected InvalidTokenException, but service continued and returned "+menu);
            } catch (InvalidTokenException e) {
                // Expected exception
            }
        });
    }

    @Test
    void unexpectedResponse() {
        withWireMock(() -> {
            stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("broken"))
                    .willReturn(aResponse().withStatus(500)));

            ApiGwMenuRepository menuRepo = new ApiGwMenuRepository(new MenuStoreClient("http://localhost:8089"));
            LocalDate date = LocalDate.of(2017, 05, 28);
            User user = User.builder().withAccessToken("broken").build();
            // There is probably a nicer way in JUnit 5, but haven't checked yet and wonder if it is compatible with this lambda wrapper
            try {
                String menu = menuRepo.whatIsForDinner(user, date);
                fail("Expected RuntimeException, but service continued and returned "+menu);
            } catch (RuntimeException e) {
                // Expected exception
                if (e instanceof InvalidTokenException) {
                    fail("Expected plain RuntimeException, but got InvalidTokenException");
                }
                assertThat(e.getMessage(), containsString("Unexpected response"));
            }
        });
    }

    @Test
    void ioProblem() {
        withWireMock(() -> {
            stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("ioProblem"))
                    .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));

            ApiGwMenuRepository menuRepo = new ApiGwMenuRepository(new MenuStoreClient("http://localhost:8089"));
            LocalDate date = LocalDate.of(2017, 05, 28);
            User user = User.builder().withAccessToken("ioProblem").build();
            // There is probably a nicer way in JUnit 5, but haven't checked yet and wonder if it is compatible with this lambda wrapper
            try {
                String menu = menuRepo.whatIsForDinner(user, date);
                fail("Expected RuntimeException, but service continued and returned "+menu);
            } catch (RuntimeException e) {
                // Expected exception
                if (e instanceof InvalidTokenException) {
                    fail("Expected plain RuntimeException, but got InvalidTokenException");
                }
                assertThat(e.getMessage(), containsString("Failed"));
            }
        });
    }

    private void withWireMock(Runnable runnable) {
        WireMockServer mockServer = new WireMockServer(wireMockConfig().port(8089));
        try {
            mockServer.start();
            WireMock.configureFor(8089);

            runnable.run();

        } finally {
            mockServer.stop();
        }
    }

}
