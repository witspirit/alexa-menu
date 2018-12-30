package be.witspirit.alexamenu;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

/**
 * Allows to respond to unmatched utterances. Problem is it will cause this to be hit too easily, because of
 * our 'optional' slot. Perhaps I can work with the different utterances of the What's for Dinner intent, but I
 * recall that I had to 'force' towards the slot type, otherwise the slot will be ignored too easily...
 */
public class FallbackHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("AMAZON.FallbackIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return new AlexaResponses(input.getResponseBuilder()).fallback();
    }
}
