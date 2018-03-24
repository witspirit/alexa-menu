package be.witspirit.alexamenu;

import be.witspirit.alexamenu.menustore.Menu;
import be.witspirit.alexamenu.menustore.MenuDao;
import be.witspirit.common.exception.InvalidTokenException;
import com.amazon.speech.speechlet.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Try to verify the behavior of the ApiGwMenuRepository
 */
public class ApiGwMenuRepositoryTest {

    @Test
    void defaultStartup() {
        // Just to check that we didn't break the default bootstrap
        new ApiGwMenuRepository();
    }

    @Test
    void successMenu() {
        ApiGwMenuRepository menuRepo = new ApiGwMenuRepository(new TestMenuDao());

        LocalDate date = LocalDate.of(2017, 05, 28);
        User user = User.builder().withAccessToken("ValidToken").build();
        String menu = menuRepo.whatIsForDinner(user, date);

        assertThat(menu).isEqualTo("TestMenu");
    }

    private class TestMenuDao implements MenuDao {
        private String accessToken;

        @Override
        public MenuDao withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        @Override
        public Menu getMenu(String dateKey) {
            if (accessToken == null || !accessToken.equals("ValidToken")) {
                throw new InvalidTokenException();
            }

            Menu testMenu = new Menu();
            testMenu.setDate(dateKey);
            testMenu.setMenu("TestMenu");
            return testMenu;
        }

        @Override
        public void setMenu(String dateKey, String dinner) {
            // Not needed here
        }
    }

}
