package be.witspirit.menuapigwlambda.api;

import be.witspirit.menuapigwlambda.menustore.MenuStore;
import be.witspirit.menuapigwlambda.security.ApiSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/suggestions")
public class SuggestionsApi {

    private final MenuStore menuStore;
    private final ApiSecurity apiSecurity;

    public SuggestionsApi(MenuStore menuStore, ApiSecurity apiSecurity) {
        this.menuStore = menuStore;
        this.apiSecurity = apiSecurity;
    }

    // It seems logical this is a GET as it will not modify state. On the other hand, it will often return different results,
    // so it is not 'really' idempotent. So perhaps a POST would be considered logical as well. So allowing both for now.
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/dinner")
    public List<String> dinnerSuggestions(@RequestParam(name = "nrOfSuggestions", defaultValue = "3") String nrOfSuggestions) {
        return menuStore.getDinnerSuggestions(apiSecurity.getAmazonUserId(), LambdaWorkarounds.intParameter(nrOfSuggestions, 3));
    }
}
