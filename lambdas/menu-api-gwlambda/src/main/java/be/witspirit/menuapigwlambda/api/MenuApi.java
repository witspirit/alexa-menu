package be.witspirit.menuapigwlambda.api;

import be.witspirit.menuapigwlambda.LocalDateFormatter;
import be.witspirit.menuapigwlambda.menustore.MenuRecord;
import be.witspirit.menuapigwlambda.menustore.MenuStore;
import be.witspirit.menuapigwlambda.security.ApiSecurity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = {"/menus"})
public class MenuApi {

    private final MenuStore menuStore;
    private final ApiSecurity apiSecurity;

    public MenuApi(MenuStore menuStore, ApiSecurity apiSecurity) {
        this.menuStore = menuStore;
        this.apiSecurity = apiSecurity;
    }

    // Full JSON Structure

    @GetMapping
    public List<UserMenu> getMenus(@RequestParam(name = "since", required = false) @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate since,
                                 @RequestParam(name= "nrOfDays", defaultValue = "7") String nrOfDays) { // Switched to String to avoid conversion issues in the Lambda wrapper
        if (since == null) { // Unfortunately I cannot set LocalDate.now() as a default value
            since = LocalDate.now();
        }
        return UserMenuConverter.toMenus(menuStore.getNext(apiSecurity.getAmazonUserId(), since, LambdaWorkarounds.intParameter(nrOfDays, 7)));
    }

    @PutMapping
    public void setMenus(@RequestBody List<UserMenu> menus) {
        for (UserMenu menu : menus) {
            menuStore.set(new MenuRecord().setUserId(apiSecurity.getAmazonUserId()).setDate(menu.getDate()).setDinner(menu.getDinner()));
        }
    }

    // Plain Text one-by-one approach

    @GetMapping("/{date}")
    public String getMenu(@PathVariable("date") @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate date) {
        return menuStore.get(apiSecurity.getAmazonUserId(), date).getDinner();
    }

    @PutMapping("/{date}")
    public void setMenu(@PathVariable("date") @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate date, @RequestBody String dinner) {
        menuStore.set(new MenuRecord().setUserId(apiSecurity.getAmazonUserId()).setDate(date).setDinner(dinner));
    }

    @DeleteMapping("/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("date") @DateTimeFormat(pattern= LocalDateFormatter.FORMAT) LocalDate date) {
        menuStore.delete(apiSecurity.getAmazonUserId(), date);
    }

}
