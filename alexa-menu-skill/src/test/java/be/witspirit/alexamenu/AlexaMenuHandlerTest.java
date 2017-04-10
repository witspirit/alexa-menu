package be.witspirit.alexamenu;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
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

        assertThat(json.read("$.response.outputSpeech.text"), is("Test Recipe"));
    }

    private DocumentContext request(String requestName) throws IOException {
        try (FileInputStream requestStream = new FileInputStream("src/test/resources/requests/"+requestName+".json");
             ByteArrayOutputStream responseStream = new ByteArrayOutputStream();) {

            AlexaMenuHandler handler = new AlexaMenuHandler(new TestMenuRepository());
            handler.handleRequest(requestStream, responseStream, null);

            DocumentContext json = JsonPath.parse(responseStream.toString());

            System.out.println(json.jsonString());

            return json;
        }
    }

    private static class TestMenuRepository implements MenuRepository {

        @Override
        public String whatIsForDinner(LocalDate date) {
            return "Test Recipe";
        }
    }
}
