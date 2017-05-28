package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * Small helper class to interactively experiment with the ApiGwMenuRepository.
 */
@Disabled("Requires a particular environment setup and is meant as an interactive tool, rather than a test")
public class ApiGwMenuRepositoryExperimenter {
    private static final String TEMP_VALID_TOKEN = "Put a valid token here to successfully use operations";

    @Test
    void retrieveMenuToday() {
        ApiGwMenuRepository repo = new ApiGwMenuRepository();
        User user = User.builder().withAccessToken(TEMP_VALID_TOKEN).build();
        LocalDate date = LocalDate.now().plusMonths(1);
        String menu = repo.whatIsForDinner(user, date);
        System.out.println("Menu for "+date+" : "+menu);
    }
}
