package termsandservice;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.github.GitHubSignInPage;
import pages.signin.EmboldSignInPage;
import pages.termsandservice.TermsAndServicePage;


@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Terms and Services")
public class TermsAndServicePageTests extends Elemental {

    private static final Logger logger = LogManager.getLogger(TermsAndServicePageTests.class);
    //Unused credentials for Embold Sign up. Registered with GitHub account. !!DO NOT USE ELSEWHERE!!

    private static final String TERMS_USERNAME = "emboldterms";
    private static final String TERMS_PASSWORD = "emb.p@ss1";

    TermsAndServicePage servicePage;
    EmboldSignInPage signInPage;
    GitHubSignInPage gitHubSignInPage;

    TermsAndServicePageTests() {
        super();
        servicePage = new TermsAndServicePage();
    }

    public void OpenTermsAndServicePage() {
        /*Verify the user if exists in DB later and then proceed.
        Check for both GitHub and Bitbucket sign ups, with new login
        To do: Improve flow with Website origin
         */
        signInPage = new EmboldSignInPage();
        gitHubSignInPage = new GitHubSignInPage();
        signInPage.displaySignInWithGitHubButton().click();
        waitTillRequestIsProcessed(2);
        gitHubSignInPage.signInToGitHub(TERMS_USERNAME, TERMS_PASSWORD);
        fluentWaitForWebElement("terms_service_illustration");
        waitTillRequestIsProcessed(1);
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate consent page.")
    @Description("Test Description: Validate consent page")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate new user has been made to agree to Embold's Terms and Services before accessing Embold.")
    public void validateConsentPageURL() {
        OpenTermsAndServicePage();
        Assert.assertTrue(driver.getCurrentUrl().contains("auth/consent"));
        logger.info("Consent page URL is valid.");
    }

    @Issues({@Issue("ValidateEmboldIsDisplayed")})
    @Story("Terms and Service page validations")
    @Test(description = "Validate if Embold logo is displayed.")
    @Description("Test Description: Validate if Embold logo is displayed")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Embold logo is displayed on top left corner of the page.")
    public void validateEmboldIsDisplayed() {
        Assert.assertTrue(servicePage.displayEmboldLogo().isDisplayed());
        logger.info("Embold logo is displayed on Consent page");
    }

    @Issues(@Issue("ValidateTermsAndServiceIllustration"))
    @Story("Terms and Service page validations")
    @Test(description = "Validate if Terms and Service illustration is displayed.")
    @Description("Test Description: Validate if Terms and Service illustration is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service illustration is displayed.")
    public void validateTermsAndServiceIllustration() {
        Assert.assertTrue(servicePage.displayServiceIllustration().isDisplayed());
        logger.info("Terms and Service illustratoin is displayed on Consent page");
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate if Terms and Service label is displayed.")
    @Description("Test Description: Validate if Terms and Service label is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service label is displayed.")
    public void validateTermsAndServiceLabel() {
        Assert.assertTrue(servicePage.displayTermsAndServiceLabel().isDisplayed());
        logger.info("Terms and Service label is displayed.");
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate if Terms and Service Description is displayed.")
    @Description("Test Description: Validate if Terms and Service Description is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service Description is displayed.")
    public void validateTermsAndServiceDescription() {
        Assert.assertTrue(servicePage.displayTermsAndServiceDescription().isDisplayed());
        logger.info("Terms and Service Description is displayed.");
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate Terms and Service Consent checkbox.")
    @Description("Test Description: Validate if Terms and Service Consent checkbox is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service Consent checkbox is displayed.")
    public void validateTermsAndServiceConsentCheckbox() {
        servicePage.displayConsentCheckbox().click();
        Assert.assertTrue(servicePage.displayConsentCheckbox().isDisplayed());
        logger.info("Terms and Service Consent checkbox is displayed.");
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate Terms and Service Consent description.")
    @Description("Test Description: Validate if Terms and Service Consent description is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service Consent description is displayed.")
    public void validateTermsAndServiceConsentDescription() {
        fluentWaitForWebElement("terms_Service_AgreeText");
        String agree = servicePage.displayConsentText().getText();
        Assert.assertEquals(agree, "I agree with Embold's");
        logger.info("Terms and Service Consent description is displayed.");
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate Terms and Service Consent link page.")
    @Description("Test Description: Validate if Terms and Service Consent link is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service Consent page link is displayed.")
    public void validateTermsAndServiceConsentLink() {
        Assert.assertTrue(servicePage.displayConsentLink().isDisplayed());
        logger.info("Terms and Service Consent page link is displayed.");
    }

    @Story("Terms and Service page validations")
    @Test(description = "Validate Terms and Service submit button.")
    @Description("Test Description: Validate if Terms and Service submit button is displayed.")
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Validate if Terms and Service submit is displayed.")
    public void validateTermsAndServiceSubmitButton() {
        Assert.assertTrue(servicePage.displaySubmitButton().isDisplayed());
        logger.info("Terms and Service submit is displayed.");
    }
}
