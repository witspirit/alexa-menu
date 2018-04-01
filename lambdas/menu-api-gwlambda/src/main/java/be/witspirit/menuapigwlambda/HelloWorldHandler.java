package be.witspirit.menuapigwlambda;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/hello")
public class HelloWorldHandler {

    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }

}
