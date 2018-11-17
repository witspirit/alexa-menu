package be.witspirit.alexamenu.menustore;

import java.time.LocalDate;

public interface MenuStore {
    String get(String userId, LocalDate date);
}
