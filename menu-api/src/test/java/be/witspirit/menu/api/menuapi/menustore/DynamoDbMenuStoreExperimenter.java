package be.witspirit.menu.api.menuapi.menustore;


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
        menuStore = new DynamoDBMenuStore();
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
}
