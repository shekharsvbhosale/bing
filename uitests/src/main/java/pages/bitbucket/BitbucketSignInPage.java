package pages.bitbucket;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import pages.addrepositories.AddRepositoriesPage;
import pages.administration.onboarding.OnboardingForNewOrganizationPage;
import pages.repositorylist.RepositoryListPage;
import pages.termsandservice.TermsAndServicePage;

public class BitbucketSignInPage extends Elemental {

    private static final Logger logger = LogManager.getLogger(BitbucketSignInPage.class);
    private static final String BITBUCKET_SIGNIN_PAGE_URL = "https://id.atlassian.com/login?application=bitbucket";

    public WebElement displayBitbucketUsernameField() {
        return getWebElement("bitbucket_username_field");
    }

    public WebElement displayBitbucketContinueButton() {
        return getClickableElement("bitbucket_continue_button", 10);
    }

    public WebElement displayBitbucketPasswordField() {
        return getClickableElement("bitbucket_password_field", 10);
    }

    public WebElement displayBitbucketLoginButton() {
        return getClickableElement("bitbucket_login_button", 10);
    }

    public boolean verifyBitbucketSignInPageURL() {
        return driver.getCurrentUrl().contains(BITBUCKET_SIGNIN_PAGE_URL);
    }

    public WebElement verifyIncorrectCredentials() {
        try {
            element = getWebElement("bitbucket_invalidCredentials_label");
        } catch (NoSuchElementException e) {
            logger.error("Incorrect Credentials message not found.");
        }
        return element;
    }

    public boolean checkIfBitbucketAccountIsVerified() {
        boolean result;
        if (System.getProperty("bbuser") == null || System.getProperty("bbpass") == null) {
            return !signInToBitbucket(System.getenv("bbuser"), System.getenv("bbpass"));
        } else {
            result = signInToBitbucket(System.getProperty("bbuser"), System.getProperty("bbpass"));
            //Add case if both env var and property value is null. Terminate execution in such case.
        }
        return !result;
    }

    public boolean signInToBitbucket(String username, String password) {
        if (verifyBitbucketSignInPageURL()) {
            displayBitbucketUsernameField().sendKeys(username);
        }
        displayBitbucketContinueButton().click();
        fluentWaitForWebElement("bitbucket_password_field");
        displayBitbucketPasswordField().sendKeys(password);
        displayBitbucketLoginButton().click();
        //Get api response from Bitbucket here. Till then use hard wait.
        waitTillRequestIsProcessed(5);

        //To do: Add Unverified BB account use case here later.
        if (InstallEmboldAppOnBitbucket.verifyGrantAccessPageURL()) {
            bbSignInState = true;
            logger.info("Bitbucket login successful.");
        } else {
            bbSignInState = true;
        }
        logger.info(bbSignInState);
        return bbSignInState;
    }

    //To do: 1. Refactor this method 2. Make this method generic for all types of supported SCMs
    public void accessEmbold(InstallEmboldAppOnBitbucket appOnBitbucket,
                             TermsAndServicePage servicePage,
                             OnboardingForNewOrganizationPage organizationPage,
                             RepositoryListPage repositoryListPage,
                             AddRepositoriesPage addRepositoriesPage) {
        waitTillRequestIsProcessed(2);
        if (servicePage.verifyConsentPageURL()) {
            logger.info("Accepting Terms and Service agreement...");
            servicePage.acceptTermsAndService();
        }
        if (InstallEmboldAppOnBitbucket.verifyGrantAccessPageURL()) {
            logger.info("Authorizing Embold App on Bitbucket...");
            appOnBitbucket.authorizeEmboldAppOnBitbucket();
            waitTillRequestIsProcessed(4);
            if (addRepositoriesPage.verifyAddRepositoriesPageURL()) {
                addRepositoriesPage.displayLinkRepositoriesIllustration();
                logger.info("No repository is added to Embold. Adding repository to Embold...");
                waitTillRequestIsProcessed(1);
                addRepositoriesPage.setRepositoryName();
                addRepositoriesPage.addBitbucketRepositoriesToEmbold(organizationPage);
                waitTillRequestIsProcessed(2);
            }
        }
        if (addRepositoriesPage.verifyAddRepositoriesPageURL()) {
            addRepositoriesPage.displayLinkRepositoriesIllustration();
            logger.info("No repository is added to Embold. Adding repository to Embold...");
            waitTillRequestIsProcessed(4);
            addRepositoriesPage.setRepositoryName();
            addRepositoriesPage.addBitbucketRepositoriesToEmbold(organizationPage);
            waitTillRequestIsProcessed(2);
        }
        if (organizationPage.verifyOnboardingPageURL()) {
            logger.info("User is on onboarding page.");
            //Commented below for MBLD2020-3070
            /*if (organizationPage.displayWelcomeAboardMessage().isDisplayed()) {
                logger.info("Welcome aboard message displayed.");
                organizationPage.addGitHubOrganizationToEmbold(appOnBitbucket);
            }*/
            //Commented below for MBLD2020-3071
            /*else if (organizationPage.displayInstallEmboldAppWarning().isDisplayed()) {
                logger.info("Install Embold app warning displayed.");
                organizationPage.installEmboldAppOnBitbucket(appOnBitbucket);
                waitTillRequestIsProcessed(1);
            }*/
            if (addRepositoriesPage.verifyAddRepositoriesPageURL()) {
                addRepositoriesPage.displayLinkRepositoriesIllustration();
                logger.info("No repository is added to Embold. Adding repository to Embold...");
                waitTillRequestIsProcessed(4);
                addRepositoriesPage.setRepositoryName();
                addRepositoriesPage.addBitbucketRepositoriesToEmbold(organizationPage);
            }
            if (organizationPage.displayLoggedInUserSCMAccountName().isDisplayed()) {
                fluentWaitForWebElement("firstAddOrg_button");
                organizationPage.displayAddOrganizationButton().click();
                fluentWaitForWebElement("freePlanGetStarted_button");
                organizationPage.selectPricingPlan();
                if (addRepositoriesPage.verifyAddRepositoriesPageURL()) {
                    addRepositoriesPage.displayLinkRepositoriesIllustration();
                    logger.info("No repository is added to Embold. Adding repository to Embold...");
                    waitTillRequestIsProcessed(4);
                    addRepositoriesPage.setRepositoryName();
                    addRepositoriesPage.addBitbucketRepositoriesToEmbold(organizationPage);
                }
            }
        }
        if (RepositoryListPage.verifyRepositoryListPageURL()) {
            logger.info("Repository List page is displayed.");
            repositoryListPage.displayEmboldLogoOnRepositoryListPage();
        }
    }

}
