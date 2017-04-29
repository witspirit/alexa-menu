package be.witspirit.alexamenu.support;

import be.witspirit.alexamenu.ProfileDeserializeTest;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Helpers to get Test Resources
 */
public class TestResources {

    public static String classpath(String resourceLocation) {
        try {
            return IOUtils.toString(TestResources.class.getResourceAsStream(resourceLocation), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test resource at "+resourceLocation, e);
        }
    }
}
