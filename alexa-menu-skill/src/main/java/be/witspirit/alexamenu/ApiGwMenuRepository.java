package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MenuRepository backed by API Gateway interface for the Menu Store
 */
public class ApiGwMenuRepository implements MenuRepository {

    @Override
    public String whatIsForDinner(User user, LocalDate date) {
        String accessToken = user.getAccessToken();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateKey = dateFormat.format(date);



        return null;
    }
}
