package be.witspirit.alexamenu;

import be.witspirit.alexamenu.menustore.MenuStore;
import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.common.exception.InvalidTokenException;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.User;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.response.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class WhatsForDinnerHandler implements RequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WhatsForDinnerHandler.class);

    private final ProfileService profileService;
    private final MenuStore menuStore;

    public WhatsForDinnerHandler(ProfileService profileService, MenuStore menuStore) {
        this.profileService = profileService;
        this.menuStore = menuStore;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("WhatsForDinnerIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        User user = input.getRequestEnvelope().getSession().getUser();
        IntentRequest request = (IntentRequest) input.getRequestEnvelope().getRequest();
        Slot dateSlot = request.getIntent().getSlots().get("Date");
        return dinner(user, parseDate(dateSlot), input.getResponseBuilder());
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

    private Optional<Response> dinner(User user, LocalDate date, ResponseBuilder responseBuilder) {
        LOG.info("Producing dinner response for {}", date);

        AlexaResponses alexaResponses = new AlexaResponses(responseBuilder);
        return retrieveProfile(user)
                .map(p -> alexaResponses.dinner(date, menuStore.get(p.getUserId(), date)))
                .orElse(alexaResponses.linkAccount());
    }
}
