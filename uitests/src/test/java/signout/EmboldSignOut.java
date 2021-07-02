package signout;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.commons.PrimaryHeader;
import pages.signout.EmboldSignOutPanel;

import static org.testng.Assert.assertEquals;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Sign out Page")
public class EmboldSignOut extends Elemental {

    private static final Logger logger = LogManager.getLogger(EmboldSignOut.class);
    static PrimaryHeader primaryHeader = new PrimaryHeader();

    @Issues({@Issue("signOutFromEmbold")})
    @Story("Sign out from Embold")
    @Test(description = "Validate if user is able to Sign out from Embold.")
    @Description("Test Description: User should be able to Sign out from Embold.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("1. Sign in to Embold. 2. Click on user profile picture in primary navigation bar. 3. Click on sign out button.")
    public static void signOutFromEmbold() throws NoSuchElementException {
        EmboldSignOutPanel signOutPanel = new EmboldSignOutPanel();
        try {
            waitTillRequestIsProcessed(2);
            fluentWaitWithCustomTimeout("loggedInUserAvatar", 6).click();
            fluentWaitWithCustomTimeout("signOutButton", 2);
            logger.info("Sign out panel opened.");
            signOutPanel.displaySignOutButton().click();
            logger.info("Clicked on sign out button.");
            if (ghSignInState == true || bbSignInState == true) {
                ghSignInState = false;
                bbSignInState = false;
            }
        } catch (NoSuchElementException e) {
            logger.error("Sign out element not found", e);
        }
        assertEquals(driver.getTitle(), "Embold | Next Level Static Code Analysis");
        fluentWaitForWebElement("login_illustration");
        logger.info("Signed out from Embold successfully.");
    }

}
