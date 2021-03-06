package be.witspirit.menuapigwlambda.alexa;

import be.witspirit.amazonlogin.AmazonProfile;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.LinkAccountCard;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Groups our responses
 */
public class AlexaResponses {
    private static final Logger LOG = LoggerFactory.getLogger(AlexaResponses.class);

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

        LOG.debug("Reprompt response with message '{}'", speechText);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }


    /**
     * Creates a {@code SpeechletResponse} for the WhatsForDinner intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    public SpeechletResponse dinner(LocalDate date, String dinner) {
        if (dinner == null || dinner.trim().isEmpty()) {
            return tell("We haven't decided yet");
        } else {
            return tell(dinner);
        }
    }

    private SpeechletResponse tell(String speechText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Menu");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        LOG.debug("Tell response with message '{}'", speechText);
        return SpeechletResponse.newTellResponse(speech, card);
    }

    public SpeechletResponse linkAccount() {
        SpeechletResponse linkAccount = tell("Please link an Amazon account to get your personal menu storage");
        LinkAccountCard accountCard = new LinkAccountCard();
        accountCard.setTitle("Menu");
        linkAccount.setCard(accountCard);
        return linkAccount;
    }

    public SpeechletResponse profile(AmazonProfile amazonProfile) {
        return tell("Hi "+amazonProfile.getName()+". " +
                "We can reach you on "+amazonProfile.getEmail()+
                " and will identify you using userId "+amazonProfile.getUserId());
    }
}
