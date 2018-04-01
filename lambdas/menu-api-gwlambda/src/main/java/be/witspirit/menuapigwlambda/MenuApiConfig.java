package be.witspirit.menuapigwlambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MenuApiConfig extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MenuApiConfig.class, args);
    }

}
