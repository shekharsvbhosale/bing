package pages.signout;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import pages.commons.PrimaryHeader;

public class EmboldSignOutPanel extends Elemental {

    private static final Logger logger = LogManager.getLogger(EmboldSignOutPanel.class);

    public EmboldSignOutPanel() {
        super();
    }

    public WebElement displaySignOutButton() {
        return getClickableElement("signOutButton", 3);
    }

    public void signOutFromEmbold(PrimaryHeader primaryHeader) throws NoSuchElementException {
        primaryHeader.displayUserAvatarInHeader().click();
        getClickableElement("signOutButton", 2);
        logger.info("Sign out panel opened.");
        displaySignOutButton().click();
        logger.info("Clicked on sign out button.");
        fluentWaitForWebElement("login_illustration");
    }
}
