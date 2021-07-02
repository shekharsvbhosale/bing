package pages.github;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

public class InstallEmboldAppOnGitHubPage extends Elemental {

    private static final Logger logger = LogManager.getLogger(InstallEmboldAppOnGitHubPage.class);

    public static WebElement displayAuthorizeOrgButton() {
        return getClickableElement("authorize_button", 3);
    }

    public static WebElement displayInstallAppButton() {
        return getClickableElement("install_app_button", 3);
    }

    public WebElement displayEmboldOrgName() {
        return getWebElement("embold_orgName");
    }

    public WebElement displayVerifyIdentityText() {
        return getWebElement("identity_text");
    }

    public boolean verifyAuthorizationPageURL() {
        return driver.getCurrentUrl().contains("login/oauth/authorize");
    }


    public boolean verifyInstallAppPageURL() {
        return driver.getCurrentUrl().contains("/installations/new/permissions");
    }

    public WebElement displayOrganizationToBeInstalled() {
        return getClickableElement("orgToBeInstalledName", 3);
    }

    public boolean verifyNewInstallationGHPageURL() {
        return driver.getCurrentUrl().contains("/installations/new");
    }

    public void authorizeEmboldApp() {
        displayAuthorizeOrgButton().click();
        logger.info("Embold App has been authorized.");
        if (!ghSignInState) {
            ghSignInState = true;
        }
    }
}
