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
        if (accessToken == null) {
            return menuResponses.linkAccount();
        }
        try {

            Content profileResponse = Request.Get("https://api.amazon.com/user/profile")
                    .addHeader("Authorization", "bearer " + accessToken)
                    .execute()
                    .returnContent();

            LOG.info("Received profile: {}", profileResponse.toString());


            Map<String, String> profileMap = new ObjectMapper().readValue(profileResponse.toString(), new TypeReference() {});
            String name = profileMap.get("name");
            String email = profileMap.get("email");
            String userId = profileMap.get("user_id");

            return menuResponses.profile(name, email, userId);

        } catch (IOException e) {
            // Not yet sure what to do with this...
            e.printStackTrace();
            return null;
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
