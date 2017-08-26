package be.witspirit.menu.api.menuapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;

@SpringBootApplication
@EnableWebMvc
public class MenuApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuApiApplication.class, args);
	}

}
