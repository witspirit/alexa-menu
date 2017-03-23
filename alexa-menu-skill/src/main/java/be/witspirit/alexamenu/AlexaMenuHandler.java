package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.Collections;
import java.util.Set;

/**
 * Handler wrapper for the Alexa Menu Skill
 */
public class AlexaMenuHandler extends SpeechletRequestStreamHandler {

    public AlexaMenuHandler() {
        super(new AlexaMenuSpeechlet(), supportedApplicationIds());
    }

    private static Set<String> supportedApplicationIds() {
        return Collections.singleton("notyetset");
    }
}
