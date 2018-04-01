package be.witspirit.menuapigwlambda.menustore;

import java.time.LocalDate;

/**
 * Menu representation as provided by the MenuStore API
 */
public class MenuRecord {
    private String userId;
    private LocalDate date;
    private String dinner;

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDinner() {
        return dinner;
    }

    public MenuRecord setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public MenuRecord setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public MenuRecord setDinner(String dinner) {
        this.dinner = dinner;
        return this;
    }

    @Override
    public String toString() {
        return userId+" : " + date+" -> "+ dinner;
    }
}
