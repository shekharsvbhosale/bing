package pages.github;

import core.Elemental;
import org.openqa.selenium.WebElement;

public class GitHubAccountVerificationPage extends Elemental {

    public static boolean verifyAccountVerificationPageURL() {
        return driver.getCurrentUrl().contains("/account/unverified-email");
    }

    public WebElement displayGitHubLogoInHeader() {
        fluentWaitForWebElement("github_logo_inHeadder");
        return getClickableElement("github_logo_inHeadder", 10);
    }
}
