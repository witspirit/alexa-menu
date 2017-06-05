package be.witspirit.alexamenu;

import be.witspirit.alexamenu.menustore.MenuStoreClient;
import com.amazon.speech.speechlet.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MenuRepository backed by API Gateway interface for the Menu Store
 */
public class ApiGwMenuRepository implements MenuRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ApiGwMenuRepository.class);

    private final MenuStoreClient menuStoreClient;

    public ApiGwMenuRepository() {
        this(new MenuStoreClient());
    }

    public ApiGwMenuRepository(MenuStoreClient menuStoreClient) {
        this.menuStoreClient = menuStoreClient;
    }

    @Override
    public String whatIsForDinner(User user, LocalDate date) {
        LOG.debug("whatIsForDinner({}, {})", user, date);

        String accessToken = user.getAccessToken();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateKey = dateFormat.format(date);

        return menuStoreClient.withAccessToken(accessToken).getMenu(dateKey).getMenu();
    }
}
