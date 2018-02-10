package be.witspirit.menu.api.menuapi;

import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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

    @Bean
    public ServletRegistrationBean exampleServletBean(SpeechletV2 chefSkill) {
        SpeechletServlet chefSkillServlet = new SpeechletServlet();
        chefSkillServlet.setSpeechlet(chefSkill);
        ServletRegistrationBean bean = new ServletRegistrationBean(chefSkillServlet, "/chefskill/*");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean
    public AmazonDynamoDB dynamoDb() {
        // Control credentials through environment variables:
        // AWS_ACCESS_KEY_ID & AWS_SECRET_KEY
        // or via the aws configure in the AWS CLI
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("dynamodb.eu-west-1.amazonaws.com", "eu-west-1"))
                .build();
    }

}
