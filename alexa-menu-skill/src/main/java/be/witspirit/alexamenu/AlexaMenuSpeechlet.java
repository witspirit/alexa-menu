package be.witspirit.alexamenu;

import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.InvalidTokenException;
import be.witspirit.amazonlogin.ProfileService;
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

        User user = requestEnvelope.getSession().getUser();

        LOG.info("Resolving intent '{}' for user '{}'", intentName, user.getUserId());

        switch (intentName) {
            case "WhatsForDinnerIntent":
                return dinner(user, LocalDate.now());
            case "WhatsForDinnerTomorrowIntent":
                return dinner(user, LocalDate.now().plusDays(1));
            case "LinkExperimentIntent":
                return profile(user);
            case "AMAZON.HelpIntent":
            default:
                return menuResponses.help();
        }
    }

    private SpeechletResponse profile(User user) {
        String accessToken = user.getAccessToken();
        LOG.debug("Access Token : {}", accessToken);
        if (accessToken == null) {
            return menuResponses.linkAccount();
        }

        AmazonProfile amazonProfile = profileService.getProfile(accessToken);
        return menuResponses.profile(amazonProfile);
    }

    private SpeechletResponse dinner(User user, LocalDate date) {
        LOG.info("Producing dinner response for {}", date);
        try {
            // Not pre-checking the access token, as depending on the implementation, this is a don't care
            return menuResponses.dinner(date, menuRepository.whatIsForDinner(user, date));
        } catch (InvalidTokenException e) {
            // Do have to catch this exception if the implementation cares
            LOG.debug("Received InvalidTokenException while trying to obtain Menu", e);
            return menuResponses.linkAccount();
        }
    }


    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        LOG.trace("onSessionEnded");
    }


}
