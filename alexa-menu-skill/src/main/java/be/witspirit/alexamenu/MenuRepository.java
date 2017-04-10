package be.witspirit.alexamenu;

import java.time.LocalDate;

/**
 * Interface to deal with our actual Menu
 */
public interface MenuRepository {

    String whatIsForDinner(LocalDate date);
}
