package be.witspirit.menu.api.menuapi.test;

import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.menu.api.menuapi.menustore.DynamoDbSetup;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AbstractIntegrationTest.DynamoDBOverride.class)
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    protected static AmazonDynamoDB ddb;


    @BeforeClass
    public static void initDynamoDb() {
        ddb = DynamoDBEmbedded.create().amazonDynamoDB();
        DynamoDbSetup.setupMenus(ddb);
    }

    @AfterClass
    public static void shutdownDynamoDb() {
        if (ddb != null) {
            ddb.shutdown();
        }
    }

    @Before
    public void cleanupDb() {
        if (ddb != null) {
            DynamoDbSetup.resetMenus(ddb);
        }
    }

    @TestConfiguration
    public static class DynamoDBOverride {
        @Bean
        public AmazonDynamoDB dynamoDb() {
            return AbstractIntegrationTest.ddb;
        }
    }

    @Autowired
    protected MockMvc mvc;

    @MockBean
    private ProfileService profileService;

    protected MockCaller setMockProfile(String key) {
        MockCaller mockCaller = new MockCaller(key);
        when(profileService.getProfile(mockCaller.token())).thenReturn(mockCaller.profile());
        return mockCaller;
    }

    protected String testResource(String resourceLocation) {
        try {
            return IOUtils.toString(new ClassPathResource(resourceLocation).getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test resource at "+resourceLocation);
        }
    }

}
