package be.witspirit.menuapigwlambda;

import org.junit.jupiter.api.Test;

public class MenuApiBootstrapTest {

    @Test // Just check the context loads without exceptions
    void contextLoad() {
        new MenuApiBootstrap();
    }
}
