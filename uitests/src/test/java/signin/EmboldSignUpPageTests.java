package signin;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.signin.EmboldSignInPage;
import pages.signin.EmboldSignUpPage;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Embold Sign up")
public class EmboldSignUpPageTests extends Elemental {
    private static final Logger logger = LogManager.getLogger(EmboldSignUpPageTests.class);

    EmboldSignInPage signInPage;
    EmboldSignUpPage emboldSignUpPage;

    EmboldSignUpPageTests() {
        super();
        signInPage = new EmboldSignInPage();
        emboldSignUpPage = new EmboldSignUpPage();
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate page title - Sign up page.")
    @Description("Test Description: Page title for Sign up page should be 'Embold -Sign up")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if page title shown is correct for Embold Sign up page.")
    public void openEmboldSignUpPage() {
        openPlatform(getPropertyValue("appUrl"));
        signInPage.displaySignUpLink().click();
        driver.navigate().back();
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate if login illustration is displayed.")
    @Description("Test Description: Login illustration should be displayed within Sign up window.")
    @Severity(SeverityLevel.MINOR)
    @Step("Check if login illustration is displayed within Sign up window.")
    public void loginIllustrationIsDisplayed() {
        Assert.assertTrue(emboldSignUpPage.displayLoginIllustration().isDisplayed());
        logger.info("Login illustration displayed.");
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate if Embold logo is displayed.")
    @Description("Test Description: Embold logo is displayed on the top left corner of the page.")
    @Severity(SeverityLevel.MINOR)
    @Step("Check if Embold logo is displayed on top left corner of the page.")
    public void emboldLogoIsDisplayed() {
        Assert.assertTrue(emboldSignUpPage.displayEmboldLogo().isDisplayed());
        logger.info("Embold logo is displayed.");
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate if Sign up Label is displayed.")
    @Description("Test Description: Sign In Label should be displayed above login window.")
    @Severity(SeverityLevel.MINOR)
    @Step("Check if Sign up label is displayed above Sign up window.")
    public void signInLabelIsDisplayed() {
        Assert.assertTrue(emboldSignUpPage.displaySignUpLabel().isDisplayed());
        logger.info("Sign up label is displayed.");
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate if Sign up with GitHub button is displayed.")
    @Description("Test Description: Sign up with GitHub button should be displayed.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Check if Sign up with GitHub button is displayed in Sign up window.")
    public void signInWithGitHubButtonIsDisplayed() {
        Assert.assertTrue(emboldSignUpPage.displaySignUpWithGitHubButton().isDisplayed());
        logger.info("Sign with Github button is displayed.");
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate if Sign up with Bitbucket button is displayed.")
    @Description("Test Description: Sign up with Bitbucket button should be displayed.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Check if Sign up with Bitbucket button is displayed in Sign up window.")
    public void signInWithBitbucketButtonIsDisplayed() {
        Assert.assertTrue(emboldSignUpPage.displaySignUpWithBitbucketButton().isDisplayed());
        logger.info("Sign in Bitbucket button is displayed.");
    }

    @Story("Sign up page Web element validations")
    @Test(description = "Validate if Sign up link is displayed.")
    @Description("Test Description: Sign In link should be displayed below login window.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Check if Sign In link is displayed below Sign up window.")
    public void signUpLinkIsDisplayed() {
        Assert.assertTrue(emboldSignUpPage.displaySignInLink().isDisplayed());
        logger.info("Sign in link is displayed.");
    }
}
