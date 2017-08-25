package be.witspirit.menu.api.menuapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class MenuApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuApiApplication.class, args);
	}
}
