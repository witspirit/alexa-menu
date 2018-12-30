package be.witspirit.alexamenu;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

public class WrapUpHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("AMAZON.NoIntent"))
                || input.matches(Predicates.intentName("AMAZON.NavigateHomeIntent"))
                || input.matches(Predicates.intentName("AMAZON.CancelIntent"))
                || input.matches(Predicates.intentName("AMAZON.StopIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return new AlexaResponses(input.getResponseBuilder()).bye();
    }
}
