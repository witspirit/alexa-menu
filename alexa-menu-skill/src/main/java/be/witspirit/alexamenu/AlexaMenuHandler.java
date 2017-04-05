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
        return Collections.singleton("amzn1.ask.skill.51d5e161-2205-44b0-a0ea-aaf646d40a1e");
    }
}
