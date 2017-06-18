package be.witspirit.alexamenu.menustore;

/**
 * Data Access interface for Menus
 */
public interface MenuDao {

    /**
     * Needs to be called before any of the other operations to establish the user context
     * @param accessToken Access token representing the user. (null will raise an InvalidTokenException)
     * @return Same MenuDao instance with current accessToken state
     */
    MenuDao withAccessToken(String accessToken);

    /**
     * Retrieves a Menu from the store based on a date key in the format YYYYmmDD, e.g. 20170618
     * @param dateKey Date in the format YYYYmmDD, e.g. 20170618
     * @return The Menu for the given date.
     */
    Menu getMenu(String dateKey);

    /**
     * Sets the menu for a given date. Will overwrite whatever is already there.
     * @param dateKey Date in the format YYYYmmDD, e.g. 20170618
     * @param dinner The dinner menu for the given date
     */
    void setMenu(String dateKey, String dinner);
}
