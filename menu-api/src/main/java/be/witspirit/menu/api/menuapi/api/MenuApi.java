package be.witspirit.menu.api.menuapi.api;

import be.witspirit.menu.api.menuapi.LocalDateFormatter;
import be.witspirit.menu.api.menuapi.MenuApiApplication;
import be.witspirit.menu.api.menuapi.menustore.MenuRecord;
import be.witspirit.menu.api.menuapi.menustore.MenuStore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = {"/menus"})
public class MenuApi {

    private MenuStore menuStore;

    public MenuApi(MenuStore menuStore) {
        this.menuStore = menuStore;
    }

    // Full JSON Structure

    @GetMapping
    public List<UserMenu> getMenus(@RequestParam(name = "since", required = false) @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate since,
                                 @RequestParam(name= "nrOfDays", defaultValue = "7") int nrOfDays) {
        if (since == null) { // Unfortunately I cannot set LocalDate.now() as a default value
            since = LocalDate.now();
        }
        return UserMenuConverter.toMenus(menuStore.getNext(amazonUserId(), since, nrOfDays));
    }

    @PutMapping
    public void setMenus(@RequestBody List<UserMenu> menus) {
        for (UserMenu menu : menus) {
            menuStore.set(new MenuRecord().setUserId(amazonUserId()).setDate(menu.getDate()).setDinner(menu.getDinner()));
        }
    }

    // Plain Text one-by-one approach

    @GetMapping("/{date}")
    public String getMenu(@PathVariable("date") @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate date) {
        return menuStore.get(amazonUserId(), date).getDinner();
    }

    @PutMapping("/{date}")
    public void setMenu(@PathVariable("date") @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate date, @RequestBody String dinner) {
        menuStore.set(new MenuRecord().setUserId(amazonUserId()).setDate(date).setDinner(dinner));
    }

    @DeleteMapping("/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("date") @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate date) {
        menuStore.delete(amazonUserId(), date);
    }

    private String amazonUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
