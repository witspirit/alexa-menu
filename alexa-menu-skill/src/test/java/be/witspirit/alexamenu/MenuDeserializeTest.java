package be.witspirit.alexamenu;

import be.witspirit.common.test.TestResources;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple unit test to verify deserialization to Menu
 */
public class MenuDeserializeTest {

    @Test
    void menu() throws IOException {
        String menuJson = TestResources.classpath("/menus/menu.json");

        Menu menu = new ObjectMapper().readValue(menuJson, Menu.class);

        assertNotNull(menu);
        assertThat(menu.getDate(), is("20170528"));
        assertThat(menu.getMenu(), is("TestMenu"));
    }
}
