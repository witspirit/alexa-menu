package be.witspirit.alexamenu.menustore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * Experimentation utility for the MenuStoreClient
 *
 * Not meant as a test, but rather as an interactive tool to try out some scenario's and observe the behavior
 */
@Disabled("Requires a particular environment setup and is meant as an interactive tool, rather than a test")
public class MenuStoreClientExperimenter {

    private static final String ACCESS_TOKEN = "Atza|IwEBIM2BpXsEdkllo6uQYbJlweRebFXuZvOHuxpvs91B86w1C0p9Kajpo3vgHkxU6FTb7QRfpx192ly6VexgJj1aivF3U6ItXiCaD4AMKjIJrmo6f68-o5Igw4YVtTEuW_TBAI5gv-drkkmQY2uaZb77313yvKnUPCmquWH-WjDQPRxrgSZejlcflA9Bc54qYobKS4N35cn9LPKOf-usulcK14a_4xkpUkpH2JaHFSB3sfA-JVSx9-dbJpF_mO0USOBaY3pAbcaq3RnQX37ejnac9Bmn0Iot0u76qRC-9gTi5OJFGAIzR80eDsUMNblD1pzg1lPHKguF73ndFH560DhFkCPJvHQC1n7O6zKS5X_MVzejm-dR_tYglLigxz0RW3oQoqjAYOhUh-vPLmDm8NZMNwNzb4U9sZbU4TXNkVLobITxVl1NDF0QlveoTSBXzzMSLxK9Og7Hs9Ui2OW7vljQCYHxjLOkXCvTinFMlo-MZnSLnykkOI3yFZ32vEZbQCzx91F0xoK7HEbTDeAR48hxVEKs"; // "<InsertValidTokenHere>";

    private static final String DEV = "https://4abokujlye.execute-api.eu-west-1.amazonaws.com/dev";
    private static final String PROD = "https://4abokujlye.execute-api.eu-west-1.amazonaws.com/prod";

    private MenuStoreClient menuStore;

    @BeforeEach
    void initMenuStore() {
        this.menuStore = new MenuStoreClient(DEV).withAccessToken(ACCESS_TOKEN);
    }

    @Test
    void displayMenu() {
        System.out.println(menuStore.getMenu("20170101"));
    }

    @Test
    void setMenu() {
        menuStore.setMenu("20170101", "Dinner"+ Instant.now());
    }

}
