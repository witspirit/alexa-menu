package be.witspirit.alexamenu;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;

import java.time.LocalDate;

/**
 * Handles the actual speech commands
 */
public class AlexaMenuSpeechlet implements SpeechletV2 {

    private final MenuResponses menuResponses;
    private final MenuRepository menuRepository;


    public AlexaMenuSpeechlet(MenuRepository menuRepository) {
        this.menuResponses = new MenuResponses();
        this.menuRepository = menuRepository;
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        return menuResponses.welcome();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        Intent intent = requestEnvelope.getRequest().getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        switch (intentName) {
            case "WhatsForDinnerIntent" : return dinner(LocalDate.now());
            case "WhatsForDinnerTomorrowIntent" : return dinner(LocalDate.now().plusDays(1));
            case "AMAZON.HelpIntent" :
            default: return menuResponses.help();
        }
    }

    private SpeechletResponse dinner(LocalDate date) {
        return menuResponses.dinner(date, menuRepository.whatIsForDinner(date));
    }


    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    }



}
