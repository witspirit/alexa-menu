package be.witspirit.alexamenu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Map based implementation of the Menu Repository
 */
public class MapMenuRepository implements MenuRepository {

    private Map<LocalDate, String> menu = new HashMap<>();;

    @Override
    public String whatIsForDinner(String userId, LocalDate date) {
        return menu.getOrDefault(date, "We haven't decided yet");
    }

    public MapMenuRepository set(int year, int month, int day, String recipe) {
        return set(LocalDate.of(year, month, day), recipe);
    }

    public MapMenuRepository set(String date, String recipe) {
        return set(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")), recipe);
    }

    public MapMenuRepository set(LocalDate date, String recipe) {
        menu.put(date, recipe);
        return this;
    }


}
