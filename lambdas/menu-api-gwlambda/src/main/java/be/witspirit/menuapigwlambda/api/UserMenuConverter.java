package be.witspirit.menuapigwlambda.api;

import be.witspirit.menuapigwlambda.menustore.MenuRecord;

import java.util.List;
import java.util.stream.Collectors;

public class UserMenuConverter {

    public static UserMenu toMenu(MenuRecord menuRecord) {
        return new UserMenu().setDate(menuRecord.getDate()).setDinner(menuRecord.getDinner());
    }

    public static List<UserMenu> toMenus(List<MenuRecord> menuRecords) {
        return menuRecords.stream().map(UserMenuConverter::toMenu).collect(Collectors.toList());
    }
}
