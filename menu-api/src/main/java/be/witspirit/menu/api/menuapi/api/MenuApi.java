package be.witspirit.menu.api.menuapi.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/menus"})
public class MenuApi {

    @GetMapping
    public String dummy() {
        return "Hello World";
    }


}
