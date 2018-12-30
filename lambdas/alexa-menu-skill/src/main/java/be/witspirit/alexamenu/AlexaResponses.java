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

    private static final String CARD_TITLE = "Dinner Wizard";

    private final ResponseBuilder responseBuilder;

    public AlexaResponses(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public Optional<Response> start() {
        return reprompt("yes ?");
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
                .withSimpleCard(CARD_TITLE, speechText)
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
                .withSimpleCard(CARD_TITLE, speechText)
                .withReprompt("Anything else ?")
                ;
    }

    public Optional<Response> linkAccount() {
        return tell("Please link an Amazon account to get your personal menu storage").withLinkAccountCard().build();
    }

    public Optional<Response> bye() {
        return responseBuilder.withSpeech("Bye")
                .withSimpleCard(CARD_TITLE, "Happy to be of assistance")
                .build();
    }

    public Optional<Response> fallback() {
        return reprompt("Couldn't quite make out what you meant. You can ask me 'what's for dinner'");
    }
}
