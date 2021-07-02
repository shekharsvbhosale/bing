package pages.administration.onboarding;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.bitbucket.InstallEmboldAppOnBitbucket;
import pages.github.InstallEmboldAppOnGitHubPage;

public class OnboardingForNewOrganizationPage extends Elemental {

    private static final Logger logger = LogManager.getLogger(OnboardingForNewOrganizationPage.class);

    public static boolean displayInstallEmboldAppWarning() {
        return getWebElement("installAppWarning_toast").isDisplayed();
    }

    public WebElement displayLoggedInUserSCMAccountName() {
        return getWebElement("scmAccountName_label");
    }

    //To do: Get list of getG web elements representing Add org button
    public WebElement displayAddOrganizationButton() {
        return getWebElement("firstAddOrg_button");
    }

    public WebElement displayGetStartedButtonOnPlansPage() {
        return getClickableElement("freePlanGetStarted_button", 10);
    }

    public WebElement displaySkipToNextButton() {
        return fluentWaitWithCustomTimeout("skipToNext_button_OrgPageBitbucket", 4);
    }

    public boolean verifyOnboardingPageURL() {
        return driver.getCurrentUrl().contains("/onboarding");
    }

    public boolean verifyPricingPlanPageURL() {
        return driver.getCurrentUrl().contains("plan?installation_id");
    }

    public WebElement displayWelcomeAboardMessage() {
        return getWebElement("welcomeAboard_label");
    }

    public WebElement displayInstallAppSuccessNotification() {
        return getWebElement("installAppSuccess_toast");
    }

    public boolean verifySuccessToastDisplayed() {
        return displayInstallAppSuccessNotification().getText().contains("already added");
    }

    public WebElement displayManageAccessLink() {
        return getWebElement("manageAccess_link");
    }

    public WebElement displayPopoverForManageAccess() {
        return getWebElement("manageAccess_popover");
    }

    public WebElement displayInstallAppLink() {
        return getClickableElement("manageAccessInstallApp_link", 5);
    }

    //Make this method generic for all types of supported SCMs
    public void addGitHubOrganizationToEmbold(InstallEmboldAppOnGitHubPage appOnGitHubPage) {
        logger.info("Adding organization to Embold...");
        if (displayAddOrganizationButton().isDisplayed()) {
            displayAddOrganizationButton().click();
            waitTillRequestIsProcessed(3);
            logger.info("Organization added to Embold.");
            if (verifyPricingPlanPageURL()) {
                logger.info("Selecting Pricing plan...");
                selectPricingPlan();
            }
            if (appOnGitHubPage.verifyInstallAppPageURL()) {
                logger.info("Installing Embold App...");
                waitTillRequestIsProcessed(2);
                InstallEmboldAppOnGitHubPage.displayInstallAppButton().click();
                waitTillRequestIsProcessed(2);
                logger.info("Embold App installed.");
            }
            if (displayInstallEmboldAppWarning()) {
                logger.info("Install Embold App warning displayed.");
                installEmboldAppOnGitHub(appOnGitHubPage);
            }
        }
        if (!ghSignInState) {
            ghSignInState = true;
        }
    }

    public void addBitbucketOrganizationToEmbold(InstallEmboldAppOnBitbucket appOnBitbucket) {
        logger.info("Adding organization to Embold...");
        if (displayAddOrganizationButton().isDisplayed()) {
            displayAddOrganizationButton().click();
            waitTillRequestIsProcessed(3);
            logger.info("Organization added to Embold.");
            if (verifyPricingPlanPageURL()) {
                logger.info("Selecting Pricing plan...");
                selectPricingPlan();
            }
            if (InstallEmboldAppOnBitbucket.verifyGrantAccessPageURL()) {
                logger.info("Installing Embold App...");
                waitTillRequestIsProcessed(2);
                appOnBitbucket.displayGrantAccessButton().click();
                waitTillRequestIsProcessed(2);
                logger.info("Embold App installed.");
            }
        }
        if (!ghSignInState) {
            ghSignInState = true;
        }
    }

    //Create seperate class for Plans, should be generic for all supported SCMs
    public void selectPricingPlan() {
        /*Add plan selection logic later*/
        waitTillRequestIsProcessed(2);
        logger.info("Selecting Free plan");
        displayGetStartedButtonOnPlansPage().click();
        logger.info("Free plan selected.");
        waitTillRequestIsProcessed(1);
    }

    public void installEmboldAppOnGitHub(InstallEmboldAppOnGitHubPage appOnGitHubPage) {
        Actions actions = new Actions(driver);
        actions.moveToElement(displayManageAccessLink()).perform();
        waitTillRequestIsProcessed(2);
        if (displayPopoverForManageAccess().isDisplayed()) {
            logger.info("Manage access popover is displayed.");
            String mainTab = driver.getWindowHandle();
            displayInstallAppLink().click();
            waitTillRequestIsProcessed(2);
            for (String handle : driver.getWindowHandles()) {
                if (!handle.equals(mainTab)) {
                    driver.switchTo().window(handle);
                    //appOnGitHubPage.displayOrganizationToBeInstalled().click();
                    waitTillRequestIsProcessed(2);
                    logger.info("Installing Embold App...");
                    InstallEmboldAppOnGitHubPage.displayInstallAppButton().click();
                    //driver.close();
                }
            }
            //Temporarily performing same actions on same tab. There is known sync issue MBLD-2862.
            // Once this is fixed, close new tab, move to parent tab and perform actions.
            //driver.switchTo().window(mainTab);
            waitTillRequestIsProcessed(2);

        }
        if (!ghSignInState) {
            ghSignInState = true;
        }
    }
}
