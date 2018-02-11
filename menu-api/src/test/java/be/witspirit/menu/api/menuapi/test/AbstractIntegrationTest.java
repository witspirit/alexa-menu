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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AbstractIntegrationTest.DynamoDBOverride.class)
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    /**
     * Switched to a ThreadLocal implementation while debugging some weird lifecycle issues when running multiple tests.
     * In my original case, I don't think I need it. But when running tests in parallel, I expect this to be needed to
     * avoid weirdness with the cleanup etc. So keeping it.
     */
    private static ThreadLocal<AmazonDynamoDB> ddb = new ThreadLocal<>();

    protected static AmazonDynamoDB ddb() {
        return ddb.get();
    }

    private static void trace(String operation, AmazonDynamoDB db) {
        // Used this method to shed some light on the lifecycle of some things. Will retain it as it may come in handy later.
        // System.out.println("============================ "+operation+" @ "+Thread.currentThread()+" on "+db+" ============================");
    }

    @BeforeClass
    public static void initDynamoDb() {
        AmazonDynamoDB dynamoDb = DynamoDBEmbedded.create().amazonDynamoDB();
        trace("initDynamoDb", dynamoDb);
        ddb.set(dynamoDb);
        DynamoDbSetup.setupMenus(dynamoDb);
    }

    @AfterClass
    public static void shutdownDynamoDb() {
        AmazonDynamoDB dynamoDb = ddb();
        trace("shutdownDynamoDb", dynamoDb);
        if (dynamoDb != null) {
            dynamoDb.shutdown();
        }
    }

    @Before
    public void cleanupDb() {
        AmazonDynamoDB dynamoDb = ddb();
        trace("cleanupDb", dynamoDb);
        if (dynamoDb != null) {
            DynamoDbSetup.resetMenus(dynamoDb);
        }
    }

    // Since we are using the same Spring Application Context definition, it gets cached and shared amongst multiple tests. While this is great
    // for performance, it does mean that stuff gets injected only once. So if we replace things, like the DynamoDB instance, we have to find
    // a way to reference the new one. In this case I just introduced a delegate so it always has the most appropriate instance.
    @TestConfiguration
    public static class DynamoDBOverride {
        @Bean
        public AmazonDynamoDB dynamoDb() {
            return (AmazonDynamoDB) Proxy.newProxyInstance(AmazonDynamoDB.class.getClassLoader(), new Class[] { AmazonDynamoDB.class }, new DynamoDbDelegate());
        }
    }

    private static class DynamoDbDelegate implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(AbstractIntegrationTest.ddb(), args);
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

    protected ResultActions apiAs(MockCaller user, MockHttpServletRequestBuilder request) {
        try {
            return mvc.perform(request.header("Authorization", "Bearer "+user.token()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform API request "+request+" for user "+user, e);
        }
    }

}
