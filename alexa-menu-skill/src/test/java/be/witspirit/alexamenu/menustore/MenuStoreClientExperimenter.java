package be.witspirit.alexamenu.menustore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * Experimentation utility for the MenuStoreClient
 *
 * Not meant as a test, but rather as an interactive tool to try out some scenario's and observe the behavior
 */
@Disabled("Requires a particular environment setup and is meant as an interactive tool, rather than a test")
public class MenuStoreClientExperimenter {

    private static final String ACCESS_TOKEN = ""; // "<InsertValidTokenHere>";

    private MenuStoreClient menuStore;

    @BeforeEach
    void initMenuStore() {
        this.menuStore = new MenuStoreClient().withAccessToken(ACCESS_TOKEN);
    }

    @Test
    void displayMenu() {
        System.out.println(menuStore.getMenu("20170101"));
    }

    @Test
    void setMenu() {
        System.out.println(menuStore.setMenu("20170101", "Dinner"+ Instant.now()));
    }

}
