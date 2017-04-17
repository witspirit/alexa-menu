package be.witspirit.alexamenu;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Handles the actual speech commands
 */
public class AlexaMenuSpeechlet implements SpeechletV2 {
    private static final Logger LOG = LoggerFactory.getLogger(AlexaMenuSpeechlet.class);

    private final MenuResponses menuResponses;
    private final MenuRepository menuRepository;


    public AlexaMenuSpeechlet(MenuRepository menuRepository) {
        LOG.trace("AlexaMenuSpeechlet - Construction");
        this.menuResponses = new MenuResponses();
        this.menuRepository = menuRepository;
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        LOG.trace("onSessionStarted");
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        LOG.trace("onLaunch");
        return menuResponses.welcome();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        LOG.trace("onIntent");
        Intent intent = requestEnvelope.getRequest().getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        String userId = requestEnvelope.getSession().getUser().getUserId();

        LOG.info("Resolving intent '{}' for user '{}'", intentName, userId);

        switch (intentName) {
            case "WhatsForDinnerIntent" : return dinner(userId, LocalDate.now());
            case "WhatsForDinnerTomorrowIntent" : return dinner(userId, LocalDate.now().plusDays(1));
            case "AMAZON.HelpIntent" :
            default: return menuResponses.help();
        }
    }

    private SpeechletResponse dinner(String userId, LocalDate date) {
        LOG.info("Producing dinner response for {}", date);
        return menuResponses.dinner(date, menuRepository.whatIsForDinner(userId, date));
    }


    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        LOG.trace("onSessionEnded");
    }



}
