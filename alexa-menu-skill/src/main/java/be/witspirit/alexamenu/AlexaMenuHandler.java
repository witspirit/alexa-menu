package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.Collections;
import java.util.Set;

/**
 * Handler wrapper for the Alexa Menu Skill
 */
public class AlexaMenuHandler extends SpeechletRequestStreamHandler {

    // Used by the normal invocation on AWS
    public AlexaMenuHandler() {
        this(new FixedMenuRepository());
    }

    public AlexaMenuHandler(MenuRepository menuRepository) {
        super(new AlexaMenuSpeechlet(menuRepository), supportedApplicationIds());
    }

    private static Set<String> supportedApplicationIds() {
        return Collections.singleton("amzn1.ask.skill.51d5e161-2205-44b0-a0ea-aaf646d40a1e");
    }
}
