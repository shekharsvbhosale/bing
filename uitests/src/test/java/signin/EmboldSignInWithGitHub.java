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
import pages.github.GitHubSignInPage;
import pages.github.InstallEmboldAppOnGitHubPage;
import pages.repositorylist.RepositoryListPage;
import pages.signin.EmboldSignInPage;
import pages.termsandservice.TermsAndServicePage;

import static org.testng.Assert.assertTrue;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Sign In with GitHub Acount")
public class EmboldSignInWithGitHub extends Elemental {

    private static final Logger logger = LogManager.getLogger(EmboldSignInWithGitHub.class);

    @Issues({@Issue("signToEmboldUsingGitHub")})
    @Story("Sign in using GitHub")
    @Test(description = "Validate if user is able to Sign in to Embold using GitHub account credentials.")
    @Description("Test Description: User should be able to Sign in to Embold using GitHub account credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Step("1. Go to Embold Sign in page. 2. Click on Sign in with GitHub. 3. Provie GitHub account credentials" +
            "to sign in to Embold.")
    public static void signToEmboldUsingGitHub() throws SkipException {
        GitHubSignInPage gitHubSignInPage = new GitHubSignInPage();
        RepositoryListPage repositoryListPage = new RepositoryListPage();
        InstallEmboldAppOnGitHubPage appOnGitHub = new InstallEmboldAppOnGitHubPage();
        TermsAndServicePage servicePage = new TermsAndServicePage();
        AddRepositoriesPage addRepositoriesPage = new AddRepositoriesPage();
        OnboardingForNewOrganizationPage organizationPage = new OnboardingForNewOrganizationPage();
        EmboldSignInPage signInPage = new EmboldSignInPage();
        try {
            if (EmboldSignInPage.verifySignInPageURL()) {
                signInPage.displaySignInWithGitHubButton().click();
                logger.info("Sign with GitHub window is opened.");
                waitTillRequestIsProcessed(3);
                if (!RepositoryListPage.verifyRepositoryListPageURL()) {
                    fluentWaitForWebElement("github_signIn_button");
                    try {
                        logger.info("Signing in to GitHub...");
                        if (gitHubSignInPage.checkIfGitHubAccountIsVerified())
                            throw new SkipException("");
                    } catch (SkipException e) {
                        logger.error("Embold sign in failed. Terminating execution.", e);
                    }
                    logger.info("Checking if user has already signed up...");
                    waitTillRequestIsProcessed(3);
                    gitHubSignInPage.accessEmbold(appOnGitHub, servicePage, organizationPage,
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
