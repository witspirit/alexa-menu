package be.witspirit.menu.api.menuapi.menustore;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Access interface for Menus
 */
public interface MenuStore {

    /**
     * Retrieves a Menu for user userId and date
     * @param userId User identification (e.g. Amazon User Id) of the owning user
     * @param date Date for which the menu is to be retrieved
     * @return The Menu for the given date.
     */
    Menu get(String userId, LocalDate date);

    /**
     * Retrieves the next nrOfDays menu's starting at (and including) the since date
     * @param userId User identification (e.g. Amazon User Id) of the owning user
     * @param since Start date (including) for which to retrieve menus
     * @param nrOfDays Nr of days for which you want to retrieve the menu
     * @return
     */
    List<Menu> getNext(String userId, LocalDate since, int nrOfDays);

    /**
     * Sets the menu. Will overwrite whatever is already there.
     * @param menu The menu record to be stored
     */
    void set(Menu menu);

    /**
     * Delete the menu entry that exists for userId and date
     * @param userId User identification (e.g. Amazon User Id) of the owning user
     * @param date Date for which the menu needs to be removed
     */
    void delete(String userId, LocalDate date);
}
