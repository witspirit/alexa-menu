package be.witspirit.alexamenu;

import be.witspirit.alexamenu.menustore.MenuStore;
import be.witspirit.alexamenu.menustore.SkillIds;
import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.common.test.EnvValue;
import be.witspirit.common.test.WithSysEnv;
import com.amazon.ask.model.Application;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Should more reflect an end-to-end test for the overall Alexa Menu Skill
 */
@Disabled("Probably need updated request JSONs for it to work again. The context is not properly built right now")
public class AlexaMenuHandlerTest {

    @Test
    void defaultStartup() {
        // Just to see if we didn't break the bootstrap
        // Also covers the implementationSelectionCheck case without environment
        new AlexaMenuHandler();
    }

    @Test
    void helpIntent() throws IOException {
        DocumentContext json = request("help");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("You can ask me 'what's for dinner'");
        assertThat(json.<String>read("$.response.card.title")).isEqualTo("Menu");
    }

    @Test
    void whatsForDinner() throws IOException {
        DocumentContext json = request("whatsfordinner");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("Today's Recipe");
    }

    @Test
    void whatIsForDinnerTomorrow() throws IOException {
        DocumentContext json = request("whatisfordinnertomorrow");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("Tomorrow's Recipe");
    }

    @Test
    void profileNoToken() throws IOException {
        DocumentContext json = request("profile_without_access_token");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("Please link an Amazon account to get your personal menu storage");
    }

    @Test
    void profileValidToken() throws IOException {
        DocumentContext json = request("profile_with_valid_token");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("Hi Tet User. We can reach you on test.user@example.com and will identify you using userId amzn1.account.TESTACCOUNTID");
    }

    @Test
    void whatIsForDinnerWithInvalidToken() throws IOException {
        DocumentContext json = request("whatsfordinner_invalidtoken");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("Please link an Amazon account to get your personal menu storage");
    }

    @Test
    void open() throws IOException {
        DocumentContext json = request("open");

        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("Welcome to the Menu skill, you can ask 'what's for dinner'");
        assertThat(json.<String>read("$.response.reprompt.outputSpeech.text")).isEqualTo("Welcome to the Menu skill, you can ask 'what's for dinner'");
    }

    @Test
    void endSession() throws IOException {
        DocumentContext json = request("end_session");

        assertThat(json.<String>read("$.version")).isEqualTo("1.0");
    }

    @Test
    void invalidIntent() throws IOException {
        DocumentContext json = request("invalid_intent");

        // If we cannot resolve the intent, we fallback to Help
        assertThat(json.<String>read("$.response.outputSpeech.text")).isEqualTo("You can ask me 'what's for dinner'");
        assertThat(json.<String>read("$.response.card.title")).isEqualTo("Menu");
    }

    private DocumentContext request(String requestName) throws IOException {
        try (FileInputStream requestStream = new FileInputStream("src/test/resources/requests/"+requestName+".json");
             ByteArrayOutputStream responseStream = new ByteArrayOutputStream()) {

            AlexaMenuHandler handler = new AlexaMenuHandler(new TestMenuStore(), new TestProfileService());

            handler.handleRequest(requestStream, responseStream, null);

            DocumentContext json = JsonPath.parse(responseStream.toString());

            System.out.println(json.jsonString());

            return json;
        }
    }

    private static class TestMenuStore implements MenuStore {

        @Override
        public String get(String userId, LocalDate date) {
            if (date.equals(LocalDate.now())) {
                return "Today's Recipe";
            } else if (date.equals(LocalDate.now().plusDays(1))) {
                return "Tomorrow's Recipe";
            }
            return "We haven't decided yet";
        }
    }

    private static class TestProfileService implements ProfileService {

        @Override
        public AmazonProfile getProfile(String accessToken) {
            if (accessToken.equals("ValidToken")) {
                AmazonProfile profile = new AmazonProfile();
                profile.setName("Tet User");
                profile.setEmail("test.user@example.com");
                profile.setUserId("amzn1.account.TESTACCOUNTID");

                return profile;

            }
            return null;
        }
    }

}
