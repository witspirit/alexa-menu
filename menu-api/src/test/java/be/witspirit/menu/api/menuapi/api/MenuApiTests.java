package be.witspirit.menu.api.menuapi.api;

import be.witspirit.menu.api.menuapi.test.AbstractIntegrationTest;
import be.witspirit.menu.api.menuapi.test.MockCaller;
import be.witspirit.menu.api.menuapi.test.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuApiTests extends AbstractIntegrationTest {

    private MockCaller user1;
    private MockCaller user2;
    private MockCaller freshUser;

    @Before
    public void loadCommonData() {
        user1 = setMockProfile("user1");
        TestDataFactory.using(ddb()).forUser(user1)
                .menu("20180201", "user1dinner1")
                .menu("20180202", "user1dinner2")
        ;

        user2 = setMockProfile("user2");
        TestDataFactory.using(ddb()).forUser(user2)
                .menu("20180201", "user2dinner1")
                .menu("20180202", "user2dinner2")
                .menu("20180203", "user2dinner3")
                .menu("20180301", "user2dinner4")
                .menu("20180303", "user2dinner5")
                .generateMenus(15, "201804%02d", "user2dinner_%02d")
        ;

        freshUser = setMockProfile("freshUser");
    }

    @Test
    public void menusOfFreshUserHasEmptyResult() throws Exception {
        apiAs(freshUser, get("/menus").param("since", "20180201"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

}
