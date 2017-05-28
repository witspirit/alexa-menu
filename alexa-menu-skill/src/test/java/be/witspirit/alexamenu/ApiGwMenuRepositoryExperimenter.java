package be.witspirit.alexamenu;

import com.amazon.speech.speechlet.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * Small helper class to interactively experiment with the ApiGwMenuRepository.
 */
public class ApiGwMenuRepositoryExperimenter {
    private static final String TEMP_VALID_TOKEN = "Atza|IwEBILT8tpmOKIBcairekcOUMmnWZl59ellym6uMpm2E22redR8LmBFQTR12N-CR8cyVTNUkRzT7epBxzQ_9gDT3YtJ83iJw9MpoyQMb5hkx_wHBJ4X-DO_UCk-YsBOz4nYMpAj7Jg_B4ZEc1WwGdaLHotukcQfYDwE66h_Qa6uWJtmJM8nl9hDzpboHwsCJBk5LkI7EytNtYXLm2_2kwLFYXGHlgPnwJ4vjIiFEsuOyWG-RWS8db_qr91TZKZu6Oui8sUV6lE5x8r_5DVg_VwjpmE8CTbXunrJqCoZoaTr7Fm-Uj-WsXHRycwS-a_5cpuSFIjWiFxbqgFXUsGF5LXgzX1zW1wPeB2b7psbK55MQPaCjKywcLtBEU6PbYVmED-CH7Nn6010EyGmOjOyeSOgHpG_ZFsHkz_N38AlCMlMWfUYwGn1FHgY-4okB-61ERPnVgA6zLhMvrPWTdBtS2ir2HgLENDECgIOQAuQZoLpg8AuPbRRZRi5Yy1q0EnILtH0XxRSG4IlL14Yd8r-pmtFkhOWhsiswOgwTHVWpeYEnNdds0w";

    @Test
    void retrieveMenuToday() {
        ApiGwMenuRepository repo = new ApiGwMenuRepository();
        User user = User.builder().withAccessToken(TEMP_VALID_TOKEN).build();
        LocalDate date = LocalDate.now().plusMonths(1);
        String menu = repo.whatIsForDinner(user, date);
        System.out.println("Menu for "+date+" : "+menu);
    }
}
