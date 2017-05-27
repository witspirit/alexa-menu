package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.User;

import java.time.LocalDate;

/**
 * Interface to deal with our actual Menu
 */
public interface MenuRepository {

    String whatIsForDinner(User user, LocalDate date);
}
