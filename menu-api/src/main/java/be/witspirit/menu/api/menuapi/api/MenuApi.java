package be.witspirit.menu.api.menuapi.api;

import be.witspirit.menu.api.menuapi.LocalDateFormatter;
import be.witspirit.menu.api.menuapi.MenuApiApplication;
import be.witspirit.menu.api.menuapi.menustore.MenuRecord;
import be.witspirit.menu.api.menuapi.menustore.MenuStore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = {"/menus"})
public class MenuApi {

    private MenuStore menuStore;

    public MenuApi(MenuStore menuStore) {
        this.menuStore = menuStore;
    }

    @GetMapping
    public List<UserMenu> next(@RequestParam(name = "since", required = false) @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate since,
                                 @RequestParam(name= "nrOfDays", defaultValue = "7") int nrOfDays) {
        if (since == null) { // Unfortunately I cannot set LocalDate.now() as a default value
            since = LocalDate.now();
        }
        return UserMenuConverter.toMenus(menuStore.getNext(amazonUserId(), since, nrOfDays));
    }

    private String amazonUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
