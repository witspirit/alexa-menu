package be.witspirit.alexamenu;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Small piece of experimentation with the Amazon DynamoDB Client
 */
@Disabled("Requires a particular environment setup and is meant as an interactive tool, rather than a test")
public class DynamoDbExperimenter {

    private static AmazonDynamoDBClient dbClient;

    @BeforeAll
    static void initClient() {
        // Control credentials through environment variables:
        // AWS_ACCESS_KEY_ID & AWS_SECRET_KEY
        // or via the aws configure in the AWS CLI
        dbClient = new AmazonDynamoDBClient();
        dbClient.setEndpoint("dynamodb.eu-west-1.amazonaws.com");
    }

    @Test
    void listTables() {
        ListTablesResult tablesResult = dbClient.listTables();
        System.out.println(tablesResult.getTableNames().toString());
    }

    @Test
    void listItemsInMenus() {
        ScanResult scanResult = dbClient.scan("menus", Arrays.asList("userId", "date", "dinner"));
        for (Map<String, AttributeValue> item : scanResult.getItems()) {
            System.out.println(item.get("userId")+";"+item.get("date")+";"+item.get("dinner"));
        }
    }

    @Test
    void writeEntry() {
        Map<String, AttributeValue> itemValues = new LinkedHashMap<>();
        itemValues.put("userId", new AttributeValue("amzn1.ask.account.AF6NFJLGC6OF6K7PVCXQMYAC2ZSMHZDATQOYPEOMQTIEWHRPZJCBF4NUC7756SLUM2YTNOP3NJBCR7EZ4LQJN6QIZT3SE5BEVO2BUB7K2MXDUDI3CUISC3WC5NDWKSF3DDCVBWV2F4L2SFXUNX6QCDKACXQHGSRBGOJHEXEDCYOM73TUZGEP5PQADFW75U6NUQ6U53MANRWCYRI"));
        itemValues.put("date", new AttributeValue("20170412"));
        itemValues.put("dinner", new AttributeValue("Pannenkoeken"));
        PutItemRequest putItemRequest = new PutItemRequest("menus", itemValues);
        dbClient.putItem(putItemRequest);
    }

    @Test
    void writeMenu() {
        set("20170410", "Macaroni");
        set("20170411", "Toscaanse Kip");
        set("20170412", "Pannenkoeken");
        set("20170413", "Diepvriespizza");
        set("20170414", "Spaghetti");

        set("20170415", "Vogelnestjes");
        set("20170416", "Macaroni");
        set("20170417", "Pizza Hut / Koude rijst");
        set("20170418", "Biefstuk Frietjes");
        set("20170419", "Pitta");
        set("20170420", "Kippebillen met rijst en curry");
        set("20170421", "Spaghetti");
    }

    @Test
    void writeMenuWeekApril22() {
        set("20170422", "Sandwiches");
        set("20170423", "Soep met balletjes");
        set("20170424", "Pragerschnitzel");
        set("20170425", "Biefstuk - Frietjes");
        set("20170426", "Croques");
        set("20170427", "Groen patatjes met worst");
        set("20170428", "Spaghetti");
    }

    private void set(String date, String dinner) {
        Map<String, AttributeValue> itemValues = new LinkedHashMap<>();
        itemValues.put("userId", new AttributeValue("amzn1.ask.account.AF6NFJLGC6OF6K7PVCXQMYAC2ZSMHZDATQOYPEOMQTIEWHRPZJCBF4NUC7756SLUM2YTNOP3NJBCR7EZ4LQJN6QIZT3SE5BEVO2BUB7K2MXDUDI3CUISC3WC5NDWKSF3DDCVBWV2F4L2SFXUNX6QCDKACXQHGSRBGOJHEXEDCYOM73TUZGEP5PQADFW75U6NUQ6U53MANRWCYRI"));
        itemValues.put("date", new AttributeValue(date));
        itemValues.put("dinner", new AttributeValue(dinner));
        PutItemRequest putItemRequest = new PutItemRequest("menus", itemValues);
        dbClient.putItem(putItemRequest);
    }

    @Test
    void getDinnerForToday() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = dateFormat.format(LocalDate.now());

        QueryRequest queryRequest = new QueryRequest("menus");
        queryRequest.addKeyConditionsEntry("userId", new Condition().withComparisonOperator("EQ").withAttributeValueList(new AttributeValue("amzn1.ask.account.AF6NFJLGC6OF6K7PVCXQMYAC2ZSMHZDATQOYPEOMQTIEWHRPZJCBF4NUC7756SLUM2YTNOP3NJBCR7EZ4LQJN6QIZT3SE5BEVO2BUB7K2MXDUDI3CUISC3WC5NDWKSF3DDCVBWV2F4L2SFXUNX6QCDKACXQHGSRBGOJHEXEDCYOM73TUZGEP5PQADFW75U6NUQ6U53MANRWCYRI")));
        queryRequest.addKeyConditionsEntry("date", new Condition().withComparisonOperator("EQ").withAttributeValueList(new AttributeValue(date)));
        QueryResult queryResult = dbClient.query(queryRequest);
        for (Map<String, AttributeValue> item : queryResult.getItems()) {
            System.out.println(item.get("userId")+";"+item.get("date")+";"+item.get("dinner"));
        }

        String dinner = queryResult.getItems().get(0).get("dinner").getS();

        System.out.println("Dinner on "+date+" is "+dinner);
    }
}