package be.witspirit.menu.api.menuapi.menustore;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DynamoDB based implementation of the Menu Store
 */
public class DynamoDBMenuStore implements MenuStore {

    public static final String DEFAULT_SERVICE_ENDPOINT = "dynamodb.eu-west-1.amazonaws.com";
    public static final String DEFAULT_REGION = "eu-west-1";

    private final AmazonDynamoDB dbClient;

    public DynamoDBMenuStore() {
        // Control credentials through environment variables:
        // AWS_ACCESS_KEY_ID & AWS_SECRET_KEY
        // or via the aws configure in the AWS CLI
        dbClient = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DEFAULT_SERVICE_ENDPOINT, DEFAULT_REGION))
                .build();
    }


    @Override
    public Menu get(String userId, LocalDate date) {
        Map<String, AttributeValue> record = getRecord(userId, dateKey(date));
        return toMenu(record);
    }


    @Override
    public List<Menu> getNext(String userId, LocalDate since, int nrOfDays) {
        return nextRecords(userId, dateKey(since), nrOfDays)
                .stream()
                .map(this::toMenu)
                .collect(Collectors.toList());
    }

    @Override
    public void set(Menu menu) {
        writeRecord(menu.getUserId(), dateKey(menu.getDate()), menu.getDinner());
    }

    @Override
    public void delete(String userId, LocalDate date) {
        deleteRecord(userId, dateKey(date));
    }

    private void deleteRecord(String userId, String date) {
        Map<String, AttributeValue> deleteKeys = new HashMap<>();
        deleteKeys.put("userId", new AttributeValue(userId));
        deleteKeys.put("date", new AttributeValue(date));
        dbClient.deleteItem("menus", deleteKeys);
    }

    private Map<String, AttributeValue> getRecord(String userId, String date) {
        Map<String, AttributeValue> keys = new HashMap<>();
        keys.put("userId", new AttributeValue(userId));
        keys.put("date", new AttributeValue(date));
        GetItemRequest lookup = new GetItemRequest("menus", keys);
        GetItemResult itemResult = dbClient.getItem(lookup);
        return itemResult.getItem();
    }

    private void writeRecord(String userId, String date, String dinner) {
        Map<String, AttributeValue> itemValues = new LinkedHashMap<>();
        itemValues.put("userId", new AttributeValue(userId));
        itemValues.put("date", new AttributeValue(date));
        itemValues.put("dinner", new AttributeValue(dinner));
        PutItemRequest putItemRequest = new PutItemRequest("menus", itemValues);
        dbClient.putItem(putItemRequest);
    }

    private List<Map<String, AttributeValue>> nextRecords(String userId, String dateKey, int nrOfRecords) {
        QueryRequest query = new QueryRequest("menus");
        query.addKeyConditionsEntry("userId", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(userId)));
        query.addKeyConditionsEntry("date", new Condition().withComparisonOperator(ComparisonOperator.GE).withAttributeValueList(new AttributeValue(dateKey)));
        query.setLimit(nrOfRecords);
        QueryResult queryResult = dbClient.query(query);

        return queryResult.getItems();
    }

    private String dateKey(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dateFormat.format(date);
    }

    private LocalDate localDate(String dateKey) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateKey, dateFormat);
    }

    private Menu toMenu(Map<String, AttributeValue> record) {
        Menu menu = new Menu();
        menu.setUserId(record.get("userId").getS())
                .setDate(localDate(record.get("date").getS()))
                .setDinner(record.get("dinner").getS());
        return menu;
    }
}
