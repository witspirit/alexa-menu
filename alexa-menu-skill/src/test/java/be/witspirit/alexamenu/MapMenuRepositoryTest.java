package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Unit test for the MapMenuRepository
 */
public class MapMenuRepositoryTest {

    private static final User DUMMY_USER = User.builder().withUserId("dummyUser").withAccessToken("dummyAccessToken").build();

    private MapMenuRepository menu;

    @BeforeEach
    void createRepository() {
        menu = new MapMenuRepository();
    }

    @Test
    void localDate() {
        menu.set(LocalDate.of(2017,04,10), "Test");
        assertThat(menu.whatIsForDinner(DUMMY_USER, LocalDate.of(2017,04,10)), is("Test"));
    }

    @Test
    void integerDate() {
        menu.set(2017,04,10, "Test");
        assertThat(menu.whatIsForDinner(DUMMY_USER, LocalDate.of(2017,04,10)), is("Test"));
    }

    @Test
    void textDate() {
        menu.set("20170410", "Test");
        assertThat(menu.whatIsForDinner(DUMMY_USER, LocalDate.of(2017, 04, 10)), is("Test"));
    }
}
