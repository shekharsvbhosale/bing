package pages.signin;

import core.Elemental;
import org.openqa.selenium.WebElement;

public class EmboldSignUpPage extends Elemental {

    public WebElement displayEmboldLogo() {
        return getWebElement("embold_logo_SignInPage");
    }

    public WebElement displayLoginIllustration() {
        return getWebElement("login_illustration");
    }

    public WebElement displaySignUpLabel() {
        return getWebElement("signUp_label");
    }

    public WebElement displaySignUpWithGitHubButton() {
        return getClickableElement("github-sign-in-button", 3);
    }

    public WebElement displaySignUpWithBitbucketButton() {
        return getClickableElement("bitbucket-sign-in-button", 3);
    }

    public WebElement displaySignInLink() {
        return getClickableElement("signIn_link", 3);
    }
}
