package be.witspirit.menu.api.menuapi;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component // Although I register it as a component, it doesn't seem to be picked up automatically
public class LocalDateFormatter implements Formatter<LocalDate> {

    public static final String FORMAT = "yyyyMMdd";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, FORMATTER);
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return FORMATTER.format(date);
    }

}
