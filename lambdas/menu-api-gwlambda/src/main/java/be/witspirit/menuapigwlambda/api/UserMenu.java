package be.witspirit.menuapigwlambda.api;

import be.witspirit.menuapigwlambda.LocalDateFormatter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * Menu representation as used in the API (and used in the context of a specific user)
 */
public class UserMenu {
    private LocalDate date;
    private String dinner;

    @JsonFormat(pattern = LocalDateFormatter.FORMAT)
    public LocalDate getDate() {
        return date;
    }

    public String getDinner() {
        return dinner;
    }

    public UserMenu setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public UserMenu setDinner(String dinner) {
        this.dinner = dinner;
        return this;
    }

    @Override
    public String toString() {
        return date+" -> "+ dinner;
    }
}
