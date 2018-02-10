package be.witspirit.menu.api.menuapi.menustore;


import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

@Ignore("Not really a test, but an interactive explorer")
public class DynamoDbMenuStoreExperimenter {
    public static final String AMAZON_USER_ID = "amzn1.account.AHMU4WP553D7BJSRDVTXSCWKYEIQ";

    private DynamoDBMenuStore menuStore;

    @Before
    public void setupMenuStore() {
        menuStore = new DynamoDBMenuStore(dynamoDb());
    }

    @Test
    public void getToday() {
        MenuRecord menuRecord = menuStore.get(AMAZON_USER_ID, LocalDate.now());

        System.out.println(menuRecord);
    }

    @Test
    public void getUpcomingWeek() {
        List<MenuRecord> upcomingWeek = menuStore.getNext(AMAZON_USER_ID, LocalDate.now(), 7);

        upcomingWeek.stream().forEach(System.out::println);
    }

    private AmazonDynamoDB dynamoDb() {
        // Control credentials through environment variables:
        // AWS_ACCESS_KEY_ID & AWS_SECRET_KEY
        // or via the aws configure in the AWS CLI
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("dynamodb.eu-west-1.amazonaws.com", "eu-west-1"))
                .build();
    }
}
