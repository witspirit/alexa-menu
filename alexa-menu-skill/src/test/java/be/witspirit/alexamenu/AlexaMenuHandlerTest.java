package be.witspirit.alexamenu;

import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * Should more reflect an end-to-end test for the overall Alexa Menu Skill
 */
public class AlexaMenuHandlerTest {

    @Test
    void helpIntent() throws IOException {
        AlexaMenuHandler handler = new AlexaMenuHandler();

        String requestJson = "{\n" +
                "  \"session\": {\n" +
                "    \"sessionId\": \"SessionId.97e4fa40-bb54-4caf-9eef-e222c51e3504\",\n" +
                "    \"application\": {\n" +
                "      \"applicationId\": \"amzn1.ask.skill.51d5e161-2205-44b0-a0ea-aaf646d40a1e\"\n" +
                "    },\n" +
                "    \"attributes\": {},\n" +
                "    \"user\": {\n" +
                "      \"userId\": \"amzn1.ask.account.AF6NFJLGC6OF6K7PVCXQMYAC2ZSMHZDATQOYPEOMQTIEWHRPZJCBF4NUC7756SLUM2YTNOP3NJBCR7EZ4LQJN6QIZT3SE5BEVO2BUB7K2MXDUDI3CUISC3WC5NDWKSF3DDCVBWV2F4L2SFXUNX6QCDKACXQHGSRBGOJHEXEDCYOM73TUZGEP5PQADFW75U6NUQ6U53MANRWCYRI\"\n" +
                "    },\n" +
                "    \"new\": true\n" +
                "  },\n" +
                "  \"request\": {\n" +
                "    \"type\": \"IntentRequest\",\n" +
                "    \"requestId\": \"EdwRequestId.aaa96365-ac68-4d51-9a97-a764321bf280\",\n" +
                "    \"locale\": \"en-US\",\n" +
                "    \"timestamp\": \"2017-04-05T18:24:46Z\",\n" +
                "    \"intent\": {\n" +
                "      \"name\": \"AMAZON.HelpIntent\",\n" +
                "      \"slots\": {}\n" +
                "    }\n" +
                "  },\n" +
                "  \"version\": \"1.0\"\n" +
                "}";
        InputStream requestStream = new ByteArrayInputStream(requestJson.getBytes());
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handler.handleRequest(requestStream, responseStream, null);
        System.out.println(responseStream.toString());
    }
}
