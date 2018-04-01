package be.witspirit.menuapigwlambda.menustore;

import be.witspirit.menuapigwlambda.LocalDateFormatter;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * DynamoDB based implementation of the Menu Store
 */
@Repository
public class DynamoDBMenuStore implements MenuStore {
    private final AmazonDynamoDB dbClient;

    public DynamoDBMenuStore(AmazonDynamoDB dbClient) {
        this.dbClient = dbClient;
    }


    @Override
    public MenuRecord get(String userId, LocalDate date) {
        return getRecord(userId, dateKey(date))
                .map(this::toMenu)
                .orElse(emptyMenu(userId, date));
    }


    @Override
    public List<MenuRecord> getNext(String userId, LocalDate since, int nrOfDays) {
        return nextRecords(userId, dateKey(since), nrOfDays)
                .stream()
                .map(this::toMenu)
                .collect(Collectors.toList());
    }

    // Extremely naive and unscalable implementation to obtain dinner suggestions !
    // Good enough to get us started with the approach, but will require some rework to make it scalable.
    @Override
    public List<String> getDinnerSuggestions(String userId, int nrOfSuggestions) {
        List<Map<String, AttributeValue>> allItemsForUser = getAllItemsForUser(userId);

        List<String> distinctValues = extractDistinct(allItemsForUser);

        return extractSuggestions(nrOfSuggestions, distinctValues);
    }

    @Override
    public void set(MenuRecord menuRecord) {
        writeRecord(menuRecord.getUserId(), dateKey(menuRecord.getDate()), menuRecord.getDinner());
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

    private Optional<Map<String, AttributeValue>> getRecord(String userId, String date) {
        Map<String, AttributeValue> keys = new HashMap<>();
        keys.put("userId", new AttributeValue(userId));
        keys.put("date", new AttributeValue(date));
        GetItemRequest lookup = new GetItemRequest("menus", keys);
        GetItemResult itemResult = dbClient.getItem(lookup);
        return Optional.ofNullable(itemResult.getItem());
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
        return LocalDateFormatter.FORMATTER.format(date);
    }

    private LocalDate localDate(String dateKey) {
        return LocalDate.parse(dateKey, LocalDateFormatter.FORMATTER);
    }

    private MenuRecord toMenu(Map<String, AttributeValue> record) {
        MenuRecord menuRecord = new MenuRecord();
        menuRecord.setUserId(record.get("userId").getS())
                .setDate(localDate(record.get("date").getS()))
                .setDinner(record.get("dinner").getS());
        return menuRecord;
    }

    private MenuRecord emptyMenu(String userId, LocalDate localDate) {
        return new MenuRecord().setUserId(userId).setDate(localDate);
    }

    private List<Map<String, AttributeValue>> getAllItemsForUser(String userId) {
        ScanRequest allMenusForUser = new ScanRequest("menus");
        allMenusForUser.addScanFilterEntry("userId", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue(userId)));
        ScanResult scanResult = dbClient.scan(allMenusForUser);
        return scanResult.getItems();
    }

    private List<String> extractDistinct(List<Map<String, AttributeValue>> items) {
        return items.stream().map(item -> item.get("dinner").getS()).distinct().collect(Collectors.toList());
    }

    private List<String> extractSuggestions(int nrOfSuggestions, List<String> allValues) {
        List<String> suggestions = new ArrayList<>(nrOfSuggestions);
        for (int i=0; i < nrOfSuggestions; i++) {
            suggestions.add(allValues.get(ThreadLocalRandom.current().nextInt(allValues.size())));
        }
        return suggestions;
    }

}
