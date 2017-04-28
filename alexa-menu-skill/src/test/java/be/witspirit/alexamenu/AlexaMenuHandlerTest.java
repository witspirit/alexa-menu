package be.witspirit.alexamenu;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Should more reflect an end-to-end test for the overall Alexa Menu Skill
 */
public class AlexaMenuHandlerTest {

    @Test
    void helpIntent() throws IOException {
        DocumentContext json = request("help");

        assertThat(json.read("$.response.outputSpeech.text"), is("You can ask me 'what's for dinner'"));
        assertThat(json.read("$.response.card.title"), is("Menu"));
    }

    @Test
    void whatsForDinner() throws IOException {
        DocumentContext json = request("whatsfordinner");

        assertThat(json.read("$.response.outputSpeech.text"), is("Today's Recipe"));
    }

    @Test
    void whatIsForDinnerTomorrow() throws IOException {
        DocumentContext json = request("whatisfordinnertomorrow");

        assertThat(json.read("$.response.outputSpeech.text"), is("Tomorrow's Recipe"));
    }

    @Test
    void profileNoToken() throws IOException {
        DocumentContext json = request("profile_without_access_token");

        assertThat(json.read("$.response.outputSpeech.text"), is("Please link an Amazon account to get your personal menu storage"));
    }

    @Test
    void profileValidToken() throws IOException {
        DocumentContext json = request("profile_with_valid_token");

        assertThat(json.read("$.response.outputSpeech.text"), is("Hi Tet User. We can reach you on test.user@example.com and will identify you using userId amzn1.account.TESTACCOUNTID"));
    }

    private DocumentContext request(String requestName) throws IOException {
        try (FileInputStream requestStream = new FileInputStream("src/test/resources/requests/"+requestName+".json");
             ByteArrayOutputStream responseStream = new ByteArrayOutputStream();) {

            AlexaMenuHandler handler = new AlexaMenuHandler(new TestMenuRepository(), new TestProfileService());
            handler.handleRequest(requestStream, responseStream, null);

            DocumentContext json = JsonPath.parse(responseStream.toString());

            System.out.println(json.jsonString());

            return json;
        }
    }

    private static class TestMenuRepository implements MenuRepository {

        @Override
        public String whatIsForDinner(String userId, LocalDate date) {
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
