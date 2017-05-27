package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for the DynamoDB Menu Repository
 */
public class DynamoDBMenuRepositoryTest {
    private static final User TEST_USER = User.builder().withUserId("TEST_USER").build();

    private AmazonDynamoDB dbMock;
    private DynamoDBMenuRepository repo;

    @BeforeEach
    void initRepo() {
        dbMock = mock(AmazonDynamoDB.class);
        repo = new DynamoDBMenuRepository(dbMock);
    }

    @Test
    void defaultStartup() {
        // Just to see if we didn't break the bootstrap
        new DynamoDBMenuRepository();
    }

    @Test
    void normalCase() {
        QueryResult result = result(item("TEST_USER", "20170417", "Today's Dinner"));
        when(dbMock.query(any())).thenReturn(result);

        String todaysDinner = repo.whatIsForDinner(TEST_USER, LocalDate.of(2017, 04, 17));
        assertThat(todaysDinner, is("Today's Dinner"));
    }

    @Test
    void noResults() {
        when(dbMock.query(any())).thenReturn(result());

        String nothingAvailable = repo.whatIsForDinner(TEST_USER, LocalDate.of(2017, 04, 17));
        assertThat(nothingAvailable, is("We haven't decided yet"));
    }

    @Test
    void multipleResults() {
        QueryResult result = result(
                item("TEST_USER", "20170417", "Dinner 1"),
                item("TEST_USER", "20170417", "Dinner 2")
        );
        when(dbMock.query(any())).thenReturn(result);

        String nothingAvailable = repo.whatIsForDinner(TEST_USER, LocalDate.of(2017, 04, 17));
        assertThat(nothingAvailable, is("We haven't decided yet"));
    }

    @Test
    void resultWithMissingDinner() {
        Map<String, AttributeValue> missingDinner = item("TEST_USER", "20170417", "Missing");
        missingDinner.remove("dinner");
        when(dbMock.query(any())).thenReturn(result(missingDinner));

        String nothingAvailable = repo.whatIsForDinner(TEST_USER, LocalDate.of(2017, 04, 17));
        assertThat(nothingAvailable, is("We haven't decided yet"));
    }

    @Test
    void resultWithNullDinner() {
        when(dbMock.query(any())).thenReturn(result(item("TEST_USER", "20170417", null)));

        String nothingAvailable = repo.whatIsForDinner(TEST_USER, LocalDate.of(2017, 04, 17));
        assertThat(nothingAvailable, is("We haven't decided yet"));
    }

    private QueryResult result(Map<String, AttributeValue>... items) {
        QueryResult queryResult = new QueryResult();
        queryResult.setItems(Arrays.asList(items));
        queryResult.setCount(items.length);
        return queryResult;
    }

    private Map<String, AttributeValue> item(String userId, String date, String dinner) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userId", new AttributeValue(userId));
        item.put("date", new AttributeValue(date));
        item.put("dinner", new AttributeValue(dinner));
        return item;
    }
}
