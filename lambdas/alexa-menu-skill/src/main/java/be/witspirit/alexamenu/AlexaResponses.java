package be.witspirit.alexamenu;

import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Groups our responses
 */
public class AlexaResponses {
    private static final Logger LOG = LoggerFactory.getLogger(AlexaResponses.class);

    private final ResponseBuilder responseBuilder;

    public AlexaResponses(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public Optional<Response> welcome() {
        return reprompt("Welcome to the Chef skill, you can ask 'what's for dinner'");
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public Optional<Response> help() {
        return reprompt("You can ask me 'what's for dinner'");
    }

    private Optional<Response> reprompt(String speechText) {
        LOG.debug("Reprompt : {}", speechText);
        return responseBuilder.withSpeech(speechText)
                .withSimpleCard("Chef", speechText)
                .withReprompt(speechText)
                .build();
    }


    /**
     * Creates a {@code SpeechletResponse} for the WhatsForDinner intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public Optional<Response> dinner(LocalDate date, String dinner) {
        if (dinner == null || dinner.trim().isEmpty()) {
            return tell("We haven't decided yet").build();
        } else {
            return tell(dinner).build();
        }
    }

    private ResponseBuilder tell(String speechText) {
        LOG.debug("Tell: {}", speechText);
        return responseBuilder.withSpeech(speechText)
                .withSimpleCard("Chef", speechText)
                ;
    }

    public Optional<Response> linkAccount() {
        return tell("Please link an Amazon account to get your personal menu storage").withLinkAccountCard().build();
    }

}
