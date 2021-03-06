package be.witspirit.menu.api.menuapi;

import be.witspirit.menu.api.menuapi.test.AbstractIntegrationTest;
import be.witspirit.menu.api.menuapi.test.MockCaller;
import be.witspirit.menu.api.menuapi.test.TestDataFactory;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Checks basic integration test setup
 */
public class MenuApiApplicationTests extends AbstractIntegrationTest {


    @Test
    public void contextLoads() {
    }

    // Two times a getMenus retrieval, just to show that the test data is cleared between tests

    @Test
    public void getMenus1() throws Exception {
        MockCaller caller = setMockProfile("valid");

        TestDataFactory.using(ddb()).forUser(caller.userId())
                .menu("20180210", "dummyDinner1")
                .menu("20180211", "dummyDinner2")
        ;

        apiAs(caller, get("/menus?since=20180210"))
                .andExpect(status().isOk())
                .andExpect(content().json(testResource("/api/menu_result_1.json")));
    }

    @Test
    public void getMenus2() throws Exception {
        MockCaller caller = setMockProfile("valid");

        TestDataFactory.using(ddb()).forUser(caller.userId())
                .menu("20180212", "dummyDinner1")
                .menu("20180213", "dummyDinner2")
        ;

        apiAs(caller, get("/menus").param("since", "20180210"))
                .andExpect(status().isOk())
                .andExpect(content().json(testResource("/api/menu_result_2.json")));
    }

}
