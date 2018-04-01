package be.witspirit.menuapigwlambda.alexa;

import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.common.exception.InvalidTokenException;
import be.witspirit.menuapigwlambda.menustore.MenuStore;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Alexa Skill Speechlet for Chef behaviors
 */
@Component
public class ChefSkill implements SpeechletV2 {
    private static final Logger LOG = LoggerFactory.getLogger(ChefSkill.class);

    private final AlexaResponses alexaResponses;
    private final MenuStore menuStore;
    private final ProfileService profileService;


    public ChefSkill(MenuStore menuStore, ProfileService profileService) {
        LOG.trace("ChefSkill - Construction");
        this.alexaResponses = new AlexaResponses();
        this.menuStore = menuStore;
        this.profileService = profileService;
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        LOG.trace("onSessionStarted");
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        LOG.trace("onLaunch");
        return alexaResponses.welcome();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        LOG.trace("onIntent");
        Intent intent = requestEnvelope.getRequest().getIntent();
        String intentName = (intent != null) ? intent.getName() : "-NoIntentMatch-"; // Not working with null to avoid issues in the switch

        User user = requestEnvelope.getSession().getUser();

        LOG.info("Resolving intent '{}' for user '{}'", intentName, user.getUserId());

        switch (intentName) { // Issue in JaCoCo to compute coverage on String Switch. Issue already identified on GitHub, but not yet merged :-(
            case "WhatsForDinnerIntent":
                Slot dateSlot = intent.getSlot("Date");
                return dinner(user, parseDate(dateSlot));
            case "LinkExperimentIntent": // Disabled this in practice, otherwise it get's precedence over the intent with missing date
                return profile(user);
            case "AMAZON.HelpIntent":
            default:
                return alexaResponses.help();
        }
    }

    private LocalDate parseDate(Slot dateSlot) {
        if (dateSlot == null) {
            LOG.info("No date slot available"); // Don't think this normally occurs
        } else {
            LOG.info("Date Slot : confirmationStatus = "+dateSlot.getConfirmationStatus()+"; name = "+dateSlot.getName()+"; value = "+dateSlot.getValue()+"; resolutions = "+dateSlot.getResolutions());
            String dateSlotValue = dateSlot.getValue();
            if (dateSlotValue != null) { // This seems to be the case if no date could be resolved
                try {
                    return LocalDate.parse(dateSlotValue);
                } catch (DateTimeParseException parseEx) {
                    LOG.warn("Failed to parse " + dateSlotValue + " as a LocalDate", parseEx);
                }
            }
        }
        LOG.info("Fallback to today...");
        return LocalDate.now();
    }

    private SpeechletResponse profile(User user) {
        return retrieveProfile(user).map(alexaResponses::profile).orElse(alexaResponses.linkAccount());
    }

    private Optional<AmazonProfile> retrieveProfile(User user) {
        String accessToken = user.getAccessToken();
        LOG.debug("Access Token : {}", accessToken);
        if (accessToken == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(profileService.getProfile(accessToken));
        } catch (InvalidTokenException e) {
            LOG.debug("Non-null access token considered invalid.", e);
            return Optional.empty();
        }
    }

    private SpeechletResponse dinner(User user, LocalDate date) {
        LOG.info("Producing dinner response for {}", date);

        return retrieveProfile(user)
                .map(p -> alexaResponses.dinner(date, menuStore.get(p.getUserId(), date).getDinner()))
                .orElse(alexaResponses.linkAccount());
    }


    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        LOG.trace("onSessionEnded");
    }


}
