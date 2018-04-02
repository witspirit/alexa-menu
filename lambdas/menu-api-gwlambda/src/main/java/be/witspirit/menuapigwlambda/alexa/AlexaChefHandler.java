package be.witspirit.menuapigwlambda.alexa;

import be.witspirit.amazonlogin.AmazonProfileService;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.menuapigwlambda.menustore.DynamoDBMenuStore;
import be.witspirit.menuapigwlambda.menustore.MenuStore;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handler wrapper for the Alexa Chef Skill
 */
public class AlexaChefHandler extends SpeechletRequestStreamHandler {

    // Used by the normal invocation on AWS
    public AlexaChefHandler() {
        this(menuStore(), new AmazonProfileService());
    }

    public AlexaChefHandler(MenuStore menuStore, ProfileService profileService) {
        super(new ChefSkill(menuStore, profileService), supportedApplicationIds());
    }

    private static Set<String> supportedApplicationIds() {
        Set<String> applicationIds = new HashSet<>();
        applicationIds.add("amzn1.ask.skill.51d5e161-2205-44b0-a0ea-aaf646d40a1e"); // Menu skill
        applicationIds.add("amzn1.ask.skill.6ea39900-5aa9-4ef4-b28f-00a95ef4f910"); // Chef skill
        return applicationIds;
    }

    private static MenuStore menuStore() {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("dynamodb.eu-west-1.amazonaws.com", "eu-west-1"))
                .build();
        return new DynamoDBMenuStore(dynamoDB);
    }
}
