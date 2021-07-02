package pages.signin;

import core.Elemental;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class EmboldSignInPage extends Elemental {

    public static boolean verifySignInPageURL() {
        return driver.getCurrentUrl().contains("/auth");
    }

    public WebElement displayEmboldLogo() throws NoSuchElementException {
        return fluentWaitWithCustomTimeout("embold_logo_SignInPage", 4);
    }

    public WebElement displayLoginIllustration() {
        return fluentWaitWithCustomTimeout("login_illustration", 4);
    }

    public WebElement displaySignInLabel() throws NoSuchElementException {
        return fluentWaitWithCustomTimeout("sign_in", 2);
    }

    public WebElement displaySignInWithGitHubButton() {
        return fluentWaitWithCustomTimeout("github-sign-in-button", 4);
    }

    public WebElement displaySignInWithBitbucketButton() {
        return fluentWaitWithCustomTimeout("bitbucket-sign-in-button", 4);
    }

    public WebElement displaySignUpLink() {
        return fluentWaitWithCustomTimeout("signup_link", 2);
    }

    //Remove below method later
    public WebElement displaySignUpLinkIntentionalyBroken() {
        return fluentWaitWithCustomTimeout("grantAccess_button", 2);
    }
}
