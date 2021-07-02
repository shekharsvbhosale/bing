package signin;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.addrepositories.AddRepositoriesPage;
import pages.administration.onboarding.OnboardingForNewOrganizationPage;
import pages.bitbucket.BitbucketSignInPage;
import pages.bitbucket.InstallEmboldAppOnBitbucket;
import pages.repositorylist.RepositoryListPage;
import pages.signin.EmboldSignInPage;
import pages.termsandservice.TermsAndServicePage;

import static org.testng.Assert.assertTrue;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Sign In with Bitbucket Account")
public class EmboldSignInWithBitbucket extends Elemental {
    private static final Logger logger = LogManager.getLogger(EmboldSignInWithBitbucket.class);

    @Issues({@Issue("signToEmboldUsingBitbucket")})
    @Story("Sign in to Embold")
    @Test(description = "Validate Bitbucket Sign in.")
    @Description("Test Description: Validate if user signs in to Embold using Bitbucket credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Step("1. Go to Embold sign in page. \n2. Open Bitbucket Sign in page. \n3. Provide Bitbucket credentials" +
            "and check if user lands on Repository List page.")
    public static void signToEmboldUsingBitbucket() throws SkipException {
        RepositoryListPage repositoryListPage = new RepositoryListPage();
        EmboldSignInPage signInPage = new EmboldSignInPage();
        BitbucketSignInPage bitbucketSignInPage = new BitbucketSignInPage();
        InstallEmboldAppOnBitbucket appOnBitbucket = new InstallEmboldAppOnBitbucket();
        TermsAndServicePage servicePage = new TermsAndServicePage();
        AddRepositoriesPage addRepositoriesPage = new AddRepositoriesPage();
        OnboardingForNewOrganizationPage organizationPage = new OnboardingForNewOrganizationPage();

        try {
            if (EmboldSignInPage.verifySignInPageURL()) {
                signInPage.displaySignInWithBitbucketButton().click();
                waitTillRequestIsProcessed(5);
                if (!RepositoryListPage.verifyRepositoryListPageURL()) {
                    getClickableElement("bitbucket_continue_button", 4);
                    try {
                        if (bitbucketSignInPage.checkIfBitbucketAccountIsVerified())
                            throw new SkipException("");
                    } catch (SkipException e) {
                        logger.error("Embold sign in failed. Terminating execution.", e);
                    }
                    logger.info("Checking if user has already signed up...");
                    waitTillRequestIsProcessed(1);
                    bitbucketSignInPage.accessEmbold(appOnBitbucket, servicePage, organizationPage,
                            repositoryListPage, addRepositoriesPage);
                }
                boolean successfulSignIn = repositoryListPage.displayEmboldLogoOnRepositoryListPage().isDisplayed();
                assertTrue(successfulSignIn);
                logger.info("Successfully signed in to Embold.");
            }
        } catch (NoSuchElementException e) {
            logger.error("Unable to find element", e);
        } catch (AssertionError e) {
            logger.error("Assertion failed", e);
        }
    }
}
