package be.witspirit.menu.api.menuapi;

import be.witspirit.menu.api.menuapi.test.AbstractIntegrationTest;
import be.witspirit.menu.api.menuapi.test.MockCaller;
import be.witspirit.menu.api.menuapi.test.TestDataFactory;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MenuApiApplicationTests extends AbstractIntegrationTest {


    @Test
    public void contextLoads() {
    }

    @Test
    public void getMenus() throws Exception {
        MockCaller caller = setMockProfile("valid");

        TestDataFactory.using(ddb).forUser(caller.userId())
                .menu("20180210", "dummyDinner1")
                .menu("20180211", "dummyDinner2")
        ;

        mvc.perform(get("/menus?since=20180210").header("Authorization", "Bearer "+caller.token()))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"date\":\"20180210\",\"dinner\":\"dummyDinner1\"},{\"date\":\"20180211\",\"dinner\":\"dummyDinner2\"}]"));
    }

    @Test
    public void getMenus2() throws Exception {
        MockCaller caller = setMockProfile("valid");

        TestDataFactory.using(ddb).forUser(caller.userId())
                .menu("20180212", "dummyDinner1")
                .menu("20180213", "dummyDinner2")
        ;

        mvc.perform(get("/menus?since=20180210").header("Authorization", "Bearer "+caller.token()))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"date\":\"20180212\",\"dinner\":\"dummyDinner1\"},{\"date\":\"20180213\",\"dinner\":\"dummyDinner2\"}]"));
    }

}
