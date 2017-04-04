package be.witspirit.alexamenu;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for the AlexaMenuSpeechlet
 */
class AlexaMenuSpeechletTest {

    private AlexaMenuSpeechlet speechlet;

    @BeforeEach
    void createSpeechletAndStart() {
        speechlet = new AlexaMenuSpeechlet();
        // Don't need the request envelope at the moment
        speechlet.onSessionStarted(null);
    }

    @AfterEach
    void endSpeechletSession() {
        if (speechlet != null) {
            // Don't need the request envelope at the moment
            speechlet.onSessionEnded(null);
        }
    }


    @Test
    void lifecycleBasics() {
        // NoOp since this is just validating the set-up and tear-down
    }

    @Test
    void onLaunch() {
        SpeechletResponse response = speechlet.onLaunch(null);
        assertNotNull(response);

        assertThat(response.getCard().getTitle(), is("Menu"));

        assertThat(response.getOutputSpeech(), instanceOf(PlainTextOutputSpeech.class));
        PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) response.getOutputSpeech();
        assertThat(outputSpeech.getText(), is("Welcome to the Menu skill, you can ask 'what's for dinner'"));

        Reprompt reprompt = response.getReprompt();
        assertNotNull(reprompt);
        assertThat(reprompt.getOutputSpeech(), instanceOf(PlainTextOutputSpeech.class));
        PlainTextOutputSpeech repromptSpeech = (PlainTextOutputSpeech) reprompt.getOutputSpeech();
        assertThat(repromptSpeech.getText(), is("Welcome to the Menu skill, you can ask 'what's for dinner'"));

        assertThat(response.getShouldEndSession(), is(false));
    }

    @Test @Disabled
    void onHelpIntent() {
        Intent intent = mock(Intent.class);
        when(intent.getName()).thenReturn("AMAZON.HelpIntent");
        SpeechletRequestEnvelope<IntentRequest> envelope = mock(SpeechletRequestEnvelope.class);
        when(envelope.getRequest().getIntent()).thenReturn(intent);
        SpeechletResponse response = speechlet.onIntent(envelope);
        assertNotNull(response);
    }

}
