package be.witspirit.alexamenu.menustore;

import be.witspirit.common.exception.InvalidTokenException;
import be.witspirit.common.test.TestResources;
import be.witspirit.common.test.WithWireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.Test;

import static be.witspirit.common.test.WireMockExtension.SERVER_URL;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Wiremock based test for the MenuStore
 */
@WithWireMock
public class ApiGwMenuDaoTest {

    @Test
    void defaultStartup() {
        // Just verifying that no errors in the constructor exist
        new ApiGwMenuDao();
    }

    @Test
    void getMenu_success() {
        stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("ValidToken"))
                .willReturn(aResponse().withStatus(200).withBody(TestResources.classpath("/menus/menu.json"))));

        ApiGwMenuDao menuClient = new ApiGwMenuDao(SERVER_URL).withAccessToken("ValidToken");

        Menu menu = menuClient.getMenu("20170528");
        assertThat(menu).isNotNull();
        assertThat(menu.getMenu()).isEqualTo("TestMenu");
        assertThat(menu.getDate()).isEqualTo("20170528");
    }

    @Test
    void getMenu_invalidToken() {
        stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("InvalidToken"))
                .willReturn(aResponse().withStatus(401)));

        ApiGwMenuDao menuClient = new ApiGwMenuDao(SERVER_URL).withAccessToken("InvalidToken");

        assertThrows(InvalidTokenException.class, () -> menuClient.getMenu("20170528"));
    }

    @Test
    void getMenu_unexpectedResponse() {
        stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("broken"))
                .willReturn(aResponse().withStatus(500)));

        ApiGwMenuDao menuClient = new ApiGwMenuDao(SERVER_URL).withAccessToken("broken");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> menuClient.getMenu("20170528"));
        assertThat(exception.getMessage()).contains("Unexpected response");
    }

    @Test
    void getMenu_ioProblem() {
        stubFor(get(urlEqualTo("/menus/20170528")).withHeader("Authorization", equalTo("ioProblem"))
                .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));

        ApiGwMenuDao menuClient = new ApiGwMenuDao(SERVER_URL).withAccessToken("ioProblem");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> menuClient.getMenu("20170528"));
        assertThat(exception.getMessage()).contains("Failed");
    }

    @Test
    void setMenu_success() {
        stubFor(put(urlEqualTo("/menus/20170718"))
                    .withRequestBody(equalTo(TestResources.classpath("/menus/set_menu.json")))
                    .withHeader("Content-Type", containing("application/json"))
                    .withHeader("Authorization", equalTo("ValidToken"))
                .willReturn(aResponse().withStatus(201)));

        ApiGwMenuDao menuClient = new ApiGwMenuDao(SERVER_URL).withAccessToken("ValidToken");

        menuClient.setMenu("20170718", "SetTestMenu");
    }
}
