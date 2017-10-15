package be.witspirit.alexamenu;

import be.witspirit.amazonlogin.AmazonProfileService;
import com.amazon.speech.speechlet.User;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Small piece of experimentation with the Amazon DynamoDB Client
 */
@Disabled("Requires a particular environment setup and is meant as an interactive tool, rather than a test")
public class DynamoDbExperimenter {

    public static final String ALEXA_USER_ID = "amzn1.ask.account.AF6NFJLGC6OF6K7PVCXQMYAC2ZSMHZDATQOYPEOMQTIEWHRPZJCBF4NUC7756SLUM2YTNOP3NJBCR7EZ4LQJN6QIZT3SE5BEVO2BUB7K2MXDUDI3CUISC3WC5NDWKSF3DDCVBWV2F4L2SFXUNX6QCDKACXQHGSRBGOJHEXEDCYOM73TUZGEP5PQADFW75U6NUQ6U53MANRWCYRI";
    public static final String AMAZON_USER_ID = "amzn1.account.AHMU4WP553D7BJSRDVTXSCWKYEIQ";
    private static AmazonDynamoDB dbClient;

    @BeforeAll
    static void initClient() {
        // Control credentials through environment variables:
        // AWS_ACCESS_KEY_ID & AWS_SECRET_KEY
        // or via the aws configure in the AWS CLI
        dbClient = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("dynamodb.eu-west-1.amazonaws.com", "eu-west-1"))
                .build();
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
            System.out.println(item.get("userId") + ";" + item.get("date") + ";" + item.get("dinner"));
        }
    }

    @Test
    void writeEntry() {
        Map<String, AttributeValue> itemValues = new LinkedHashMap<>();
        itemValues.put("userId", new AttributeValue(ALEXA_USER_ID));
        itemValues.put("date", new AttributeValue("20170412"));
        itemValues.put("dinner", new AttributeValue("Pannenkoeken"));
        PutItemRequest putItemRequest = new PutItemRequest("menus", itemValues);
        dbClient.putItem(putItemRequest);
    }

    @Test
    void todaysMenuViaMenuRepository() {
        DynamoDBMenuRepository repo = new DynamoDBMenuRepository(new AmazonProfileService());
        String dinner = repo.whatIsForDinner(
                User.builder().withUserId(ALEXA_USER_ID).build(),
                LocalDate.now());
        System.out.println("Dinner today = " + dinner);
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
    void getDinnerForToday() {
        LocalDate today = LocalDate.now();
        String date = dateKey(today);

        // date = "20170101";

        Map<String, AttributeValue> record = getRecord(AMAZON_USER_ID, date);

        if (record == null) {
            System.out.println("No dinner found for " + AMAZON_USER_ID + "-" + date);
        } else {
            String dinner = record.get("dinner").getS();
            System.out.println("Dinner on " + date + " is " + dinner);
        }

    }

    @Test
    void migrateAllAlexaAccountsToAmazonAccounts() {
        ScanRequest scanRequest = new ScanRequest("menus");
        scanRequest.addScanFilterEntry("userId", new Condition().withComparisonOperator("EQ").withAttributeValueList(new AttributeValue(ALEXA_USER_ID)));
        ScanResult scanResult = dbClient.scan(scanRequest);

        System.out.println("---------------");
        for (Map<String, AttributeValue> record : scanResult.getItems()) {

            for (String attributeKey : record.keySet()) {
                System.out.println(attributeKey + " : " + record.get(attributeKey).getS());
            }

            migrate(record);

            System.out.println("---------------");
        }

    }

    @Test
    void writeTestDate() {
        set("20170102", "Dummy Dinner");
    }

    @Test
    void nextWeek() {
        String dateKey = dateKey(LocalDate.now());

        dateKey = "20170826";

        QueryRequest query = new QueryRequest("menus");
        query.addKeyConditionsEntry("userId", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(AMAZON_USER_ID)));
        query.addKeyConditionsEntry("date", new Condition().withComparisonOperator(ComparisonOperator.GE).withAttributeValueList(new AttributeValue(dateKey)));
        query.setLimit(7);
        QueryResult queryResult = dbClient.query(query);

        System.out.println("---------------");
        for (Map<String, AttributeValue> record : queryResult.getItems()) {

            for (String attributeKey : record.keySet()) {
                System.out.println(attributeKey + " : " + record.get(attributeKey).getS());
            }

            System.out.println("---------------");
        }


    }

    @Test
    void listDistinctDinnersForUser() {
        List<Map<String, AttributeValue>> allItemsForUser = getAllItemsForUser(AMAZON_USER_ID);

        List<String> distinctValues = extractDistinct(allItemsForUser);

        System.out.println("Found "+distinctValues.size()+" distinct values for "+AMAZON_USER_ID);
        distinctValues.stream().forEach(dinner -> System.out.println(dinner));

        for (int i=0; i < 5; i++) {
            System.out.println("\nSuggestions:");
            List<String> suggestions = extractSuggestions(distinctValues);
            suggestions.stream().forEach(suggestion -> System.out.println(suggestion));
        }

    }

    private List<String> extractSuggestions(List<String> allValues) {
        Random random = new Random();
        int nrOfSuggestions = 3;
        List<String> suggestions = new ArrayList<>(nrOfSuggestions);
        for (int i=0; i < nrOfSuggestions; i++) {
            suggestions.add(allValues.get(random.nextInt(allValues.size())));
        }
        return suggestions;
    }

    private List<String> extractDistinct(List<Map<String, AttributeValue>> items) {
        return items.stream().map(item -> item.get("dinner").getS()).distinct().collect(Collectors.toList());
    }

    private List<Map<String, AttributeValue>> getAllItemsForUser(String userId) {
        ScanRequest allMenusForUser = new ScanRequest("menus");
        allMenusForUser.addScanFilterEntry("userId", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(userId)));
        ScanResult scanResult = dbClient.scan(allMenusForUser);
        return scanResult.getItems();
    }

    private void migrate(Map<String, AttributeValue> alexaRecord) {
        String date = alexaRecord.get("date").getS();
        Map<String, AttributeValue> amazonRecord = getRecord(AMAZON_USER_ID, date);
        if (amazonRecord == null) {
            System.out.println("No existing record found for " + AMAZON_USER_ID + "-" + date + " -> Migrating");
            // Amazon based record does not yet exist, so migrate
            writeRecord(AMAZON_USER_ID, date, alexaRecord.get("dinner").getS());
        } else {
            System.out.println("Existing record found for " + AMAZON_USER_ID + "-" + date + " -> Assuming more recent, so skipping migration");
        }
        // Cleanup original record
        deleteRecord(ALEXA_USER_ID, date);
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

    private void set(String date, String dinner) {
        writeRecord(AMAZON_USER_ID, date, dinner);
    }

    private void writeRecord(String userId, String date, String dinner) {
        Map<String, AttributeValue> itemValues = new LinkedHashMap<>();
        itemValues.put("userId", new AttributeValue(userId));
        itemValues.put("date", new AttributeValue(date));
        itemValues.put("dinner", new AttributeValue(dinner));
        PutItemRequest putItemRequest = new PutItemRequest("menus", itemValues);
        dbClient.putItem(putItemRequest);
    }

    private String dateKey(LocalDate date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dateFormat.format(date);
    }

}
