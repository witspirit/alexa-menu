package be.witspirit.menu.serverui;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class Ui {

    @GetMapping
    public ModelAndView root() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ModelAndView("index", "authentication", authentication);
    }
}
