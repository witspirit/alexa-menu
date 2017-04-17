package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.time.LocalDate;

/**
 * Groups our responses
 */
public class MenuResponses {

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public SpeechletResponse welcome() {
        return reprompt("Welcome to the Menu skill, you can ask 'what's for dinner'");
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public SpeechletResponse help() {
        return reprompt("You can ask me 'what's for dinner'");
    }

    private SpeechletResponse reprompt(String speechText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Menu");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }


    /**
     * Creates a {@code SpeechletResponse} for the WhatsForDinner intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public SpeechletResponse dinner(LocalDate date, String dinner) {
        return tell(dinner);
    }

    private SpeechletResponse tell(String speechText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Menu");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
}
