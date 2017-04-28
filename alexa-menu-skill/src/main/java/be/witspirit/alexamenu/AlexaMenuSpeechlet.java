package be.witspirit.alexamenu;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.LinkAccountCard;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Handles the actual speech commands
 */
public class AlexaMenuSpeechlet implements SpeechletV2 {
    private static final Logger LOG = LoggerFactory.getLogger(AlexaMenuSpeechlet.class);

    private final MenuResponses menuResponses;
    private final MenuRepository menuRepository;
    private final ProfileService profileService;


    public AlexaMenuSpeechlet(MenuRepository menuRepository, ProfileService profileService) {
        LOG.trace("AlexaMenuSpeechlet - Construction");
        this.menuResponses = new MenuResponses();
        this.menuRepository = menuRepository;
        this.profileService = profileService;
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
        String intentName = (intent != null) ? intent.getName() : "-NoIntentMatch-"; // Not working with null to avoid issues in the switch

        String userId = requestEnvelope.getSession().getUser().getUserId();

        LOG.info("Resolving intent '{}' for user '{}'", intentName, userId);

        switch (intentName) {
            case "WhatsForDinnerIntent":
                return dinner(userId, LocalDate.now());
            case "WhatsForDinnerTomorrowIntent":
                return dinner(userId, LocalDate.now().plusDays(1));
            case "LinkExperimentIntent":
                return profile(requestEnvelope.getSession().getUser().getAccessToken());
            case "AMAZON.HelpIntent":
            default:
                return menuResponses.help();
        }
    }

    private SpeechletResponse profile(String accessToken) {
        LOG.debug("Access Token : {}", accessToken);
        if (accessToken == null) {
            return menuResponses.linkAccount();
        }

        AmazonProfile amazonProfile = profileService.getProfile(accessToken);
        return menuResponses.profile(amazonProfile);
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
