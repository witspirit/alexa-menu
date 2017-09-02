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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    void writeMenuWeekApril22() {
        set("20170422", "Sandwiches");
        set("20170423", "Soep met balletjes");
        set("20170424", "Pragerschnitzel");
        set("20170425", "Biefstuk - Frietjes");
        set("20170426", "Croques");
        set("20170427", "Groen patatjes met worst");
        set("20170428", "Spaghetti");
    }

    @Test
    void writeMenuWeekApril29() {
        set("20170429", "Vogelnestjes");
        set("20170430", "Eerste Communie Liesbeth");
        set("20170501", "Kippebillen met rijst en curry");
        set("20170502", "Diepvriespizza");
        set("20170503", "Soep met balletjes");
        // set("20170504", "");
        set("20170505", "Spaghetti");
    }

    @Test
    void writeMenuWeekMay6() {
        set("20170506", "Nasi Goreng");
        set("20170507", "Spaghetti");
        set("20170508", "Mix Paysanne en Kipfilet");
        set("20170509", "Diepvriespizza");
        set("20170510", "Vol-au-vent");
        set("20170511", "Pasta met spruitjes");
        set("20170512", "Spaghetti");
    }

    @Test
    void writeMenuWeekMay13() {
        set("20170513", "Huwelijk Kurt en An");
        set("20170514", "Groumet, koude groentjes");
        set("20170515", "Hotdogs");
        set("20170516", "Diepvriespizza");
        set("20170517", "Kip met appelmoes");
        set("20170518", "Groen patatjes, worst, eitjes");
        set("20170519", "Spaghetti");
    }

    @Test
    void writeMenuWeekMay20() {
        set("20170520", "Fruit en Taart / Cassoulet");
        set("20170521", "David & Anita / Quick");
        set("20170522", "Pitta");
        set("20170523", "Diepvriespizza");
        set("20170524", "Groen petatjes, worst, eitjes");
        set("20170525", "Macaroni");
        set("20170526", "Tussendoor Snacks");
    }

    @Test
    void writeMenuWeekMay27() {
        set("20170527", "Sushi Boterhammen");
        set("20170528", "Erwtjes en worteltjes met worst");
        set("20170529", "Sushi Boot");
        set("20170530", "Diepvriespizza");
        set("20170531", "Vol-au-vent");
        set("20170601", "Koude rijst");
        set("20170602", "Spaghetti");
    }

    @Test
    void writeMenuWeekJune03() {
        set("20170603", "Verjaardag Kris");
        set("20170604", "Nasi Goreng");
        set("20170605", "Kip met appelmoes");
        set("20170606", "Diepvriespizza");
        set("20170607", "Pannenkoeken");
        set("20170608", "E&W met biefstuk");
        set("20170609", "Spaghetti");
    }

    @Test
    void writeMenuWeekJune10() {
        set("20170610", "Sandwiches");
        set("20170611", "Kip met appelmoes");
        set("20170612", "Sushiboot");
        set("20170613", "Diepvriespizza");
        set("20170614", "Pannenkoeken / groen petatjes met worst");
        set("20170615", "Mix paysanne met zwitserse schijf");
        set("20170616", "Spaghetti");
    }

    @Test
    void writeMenuWeekJune17() {
        set("20170617", "Macaroni");
        set("20170618", "Varkenshaasje met mango");
        set("20170619", "Pitta");
        set("20170620", "Diepvriespizza");
        set("20170621", "Papa take-out");
        set("20170622", "Mama take-out/worstenbroodjes");
        set("20170623", "Spaghetti");
    }

    @Test
    void writeMenuWeekJune24() {
        set("20170624", "Natascha");
        set("20170625", "Frituur");
        set("20170626", "Medaillon met appelmoes");
        set("20170627", "Nasi goreng");
        set("20170628", "Soep met balletjes");
        set("20170629", "Macaroni");
        set("20170630", "Spaghetti");
    }

    @Test
    void writeMenuWeekAugust16() {
        set("20170816", "Diepvriespizza");
        set("20170817", "Macaroni");
        set("20170818", "Spaghetti");
    }

    @Test
    void writeMenuWeekAugust26() {
        set("20170826", "Mix Paysanne + Kipfilet");
        set("20170827", "Patatjes, Pragerschnitzel, Mix Forestiere");
        set("20170828", "Restaurant");
        set("20170829", "Pitta");
        set("20170830", "Diepvriespizza");
        set("20170831", "Koude rijst");
        set("20170901", "Mama spaghetti");
    }

    @Test
    void writeMenuWeekSeptember02() {
        set("20170902", "Varkenshaasje met mango");
        set("20170903", "kippe loze vink + appelmoes");
        set("20170904", "poffertjes");
        set("20170905", "diepvriespizza");
        set("20170906", "hotdogs");
        set("20170907", "Koude rijst");
        set("20170908", "Mama spaghetti");
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
