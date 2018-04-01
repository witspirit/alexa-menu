package be.witspirit.menuapigwlambda.menustore;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

/**
 * Takes care of the setup of our DynamoDB tables
 */
public class DynamoDbSetup {

    public static void setupMenus(AmazonDynamoDB ddb) {
        CreateTableRequest menusTableRequest = new CreateTableRequest()
                .withTableName("menus")
                .withAttributeDefinitions(new AttributeDefinition("userId", ScalarAttributeType.S), new AttributeDefinition("date", ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement("userId", KeyType.HASH), new KeySchemaElement("date", KeyType.RANGE))
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
        CreateTableResult result = ddb.createTable(menusTableRequest);
    }

    public static void removeMenus(AmazonDynamoDB ddb) {
        ddb.deleteTable("menus");
    }

    public static void resetMenus(AmazonDynamoDB ddb) {
        removeMenus(ddb);
        setupMenus(ddb);
    }
}
