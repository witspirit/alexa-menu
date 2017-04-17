package be.witspirit.alexamenu;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DynamoDB backed Menu Repository
 */
public class DynamoDBMenuRepository implements MenuRepository {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBMenuRepository.class);

    private final AmazonDynamoDB dynamo;

    public DynamoDBMenuRepository() {
        this(createDefaultDynamoDBClient());
    }

    private static AmazonDynamoDB createDefaultDynamoDBClient() {
        AmazonDynamoDBClient dbClient = new AmazonDynamoDBClient();
        dbClient.setEndpoint("dynamodb.eu-west-1.amazonaws.com");
        return dbClient;
    }

    public DynamoDBMenuRepository(AmazonDynamoDB dynamo) {
        this.dynamo = dynamo;
    }

    @Override
    public String whatIsForDinner(String userId, LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateKey = dateFormat.format(date);

        QueryRequest queryRequest = new QueryRequest("menus");
        queryRequest.addKeyConditionsEntry("userId", new Condition().withComparisonOperator("EQ").withAttributeValueList(new AttributeValue(userId)));
        queryRequest.addKeyConditionsEntry("date", new Condition().withComparisonOperator("EQ").withAttributeValueList(new AttributeValue(dateKey)));
        QueryResult queryResult = dynamo.query(queryRequest);

        if (queryResult.getCount() == 1) {
            // Exactly what we expect if an entry is available
            AttributeValue dinner = queryResult.getItems().get(0).get("dinner");
            if (dinner != null) {
                if (!StringUtils.isNullOrEmpty(dinner.getS())) {
                    LOG.debug("{}-{} -> {}", userId, dateKey, dinner);
                    return dinner.getS();
                } else {
                    LOG.warn("Found an empty dinner value for {}-{}", userId, dateKey);
                }
            } else {
                LOG.error("Found an entry for {}-{}, but din't contain a {} attribute", userId, dateKey, "dinner");
            }
        } else if (queryResult.getCount() > 1) {
            LOG.error("Found multiple entries matching key {}-{}", userId, dateKey);
        } else {
            LOG.debug("No result found matching key {}-{}", userId, dateKey);
        }

        return "We haven't decided yet";
    }
}
