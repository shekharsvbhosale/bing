package pages.bitbucket;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

public class InstallEmboldAppOnBitbucket extends Elemental {
    private static final Logger logger = LogManager.getLogger(InstallEmboldAppOnBitbucket.class);

    public static boolean verifyGrantAccessPageURL() {
        return driver.getCurrentUrl().contains("https://bitbucket.org/site/oauth2");
    }

    public WebElement displayGrantAccessButton() {
        return getWebElement("grantAccess_button");
    }

    //Create generic method for all supported SCMs
    public void authorizeEmboldAppOnBitbucket() {
        displayGrantAccessButton().click();
        waitTillRequestIsProcessed(2);
        //fluentWaitWithCustomTimeout("selectFirstRepository_checkbox",2).click();
        logger.info("Embold App has been authorized.");
        if (!bbSignInState) {
            bbSignInState = true;
        }
    }
}
