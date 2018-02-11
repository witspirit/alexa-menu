package be.witspirit.menu.api.menuapi.test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Helper class to easily inject/generate test data
 */
public class TestDataFactory {
    private final AmazonDynamoDB ddb;
    private String userId = "testUser";

    private TestDataFactory(AmazonDynamoDB ddb) {
        this.ddb = ddb;
    }

    public static TestDataFactory using(AmazonDynamoDB ddb) {
        return new TestDataFactory(ddb);
    }

    public TestDataFactory forUser(String userId) {
        this.userId = userId;
        return this;
    }

    public TestDataFactory forUser(MockCaller mockCaller) {
        this.userId = mockCaller.userId();
        return this;
    }

    public TestDataFactory menu(String date, String dinner) {
        writeMenuFields(userId, date, dinner);
        return this;
    }

    private void writeMenuFields(String userId, String date, String dinner) {
        Map<String, AttributeValue> itemValues = new LinkedHashMap<>();
        itemValues.put("userId", new AttributeValue(userId));
        itemValues.put("date", new AttributeValue(date));
        itemValues.put("dinner", new AttributeValue(dinner));
        PutItemRequest putItemRequest = new PutItemRequest("menus", itemValues);
        ddb.putItem(putItemRequest);
    }

    public void generateMenus(int amount, String datePattern, String dinnerPattern) {
        IntStream.range(0, amount)
                .forEach((i) -> writeMenuFields(userId, String.format(datePattern, i), String.format(dinnerPattern, i)));
    }
}
