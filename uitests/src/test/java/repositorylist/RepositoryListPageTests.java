package repositorylist;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.commons.ToastNotifications;
import pages.repositorylist.RepositoryListPage;
import utilities.APIInvoker;

import static pages.commons.PrimaryHeader.displayRepositoriesLinkInHeader;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Repository List Page")
public class RepositoryListPageTests extends Elemental {

    private static final Logger logger = LogManager.getLogger(RepositoryListPageTests.class);
    static WebElement repoTile;
    private static boolean scanState = false;
    RepositoryListPage repositoryListPage;
    APIInvoker apiInvoker;

    RepositoryListPageTests() {
        super();
        repositoryListPage = new RepositoryListPage();
        apiInvoker = new APIInvoker();
    }

    public void waitForRepositoryScanStatus() {
        if (RepositoryListPage.verifyRepositoryListPageURL()) {
            if (RepositoryListPage.displayRepositoryTile()) {
                scanState = checkIfElementPresent("progressBarDuringScanOnRepoTile");
                if (scanState) {
                    logger.info("Waiting for repository scan to finish...");
                }
            }
        }
        if (scanState) {
            //To do: Get hard wait until API is implemented. Assert based on API response later.
            scanState = fluentWaitWithCustomTimeout("viewScanResults_Link", 60).isDisplayed();
            Assert.assertTrue(ToastNotifications.displaySuccessToastNotification().isDisplayed());
            fluentWaitWithCustomTimeout("closeButton_ToastNotification", 2).click();
            logger.info("Repository scan successful.");
            waitTillRequestIsProcessed(3);
            //displayRepositoriesLinkInHeader().click();
            scanState = false;
        } else {
            scanState = true;
        }
    }

    @Issues({@Issue("validateSearchRepositoryField")})
    @Story("Search Repository")
    @Test(description = "Validate if correct placeholder for search repository is diplayed.")
    @Description("Test Description: Correct placeholder for search repository is diplayed.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("1. Go to Repository List page. 2. Validate correct place holder is displayed.")
    public void validateSearchRepositoryField() {
        try {
            waitForRepositoryScanStatus();
            if (scanState) {
                Assert.assertTrue(repositoryListPage.displayFilterCard());
                Assert.assertTrue(repositoryListPage.displayRepositorySearchField().isDisplayed());
                logger.info("Search field on Repository List page is displayed.");
                String uiPlaceholder = repositoryListPage.displayRepositorySearchField().getAttribute("placeholder");
                String jsonPlaceholder = apiInvoker.getRepositoryListLabelsFromTranslationJSON("search_repository");
                Assert.assertEquals(uiPlaceholder, jsonPlaceholder);
                logger.info("Correct Placeholder for search field is displayed.");
            }
        } catch (AssertionError e) {
            logger.error("Validation for Search field is failed. Check stacktrace for more info.", e);
        }
        scanState = false;
    }

    @Issues({@Issue("searchRepository")})
    @Story("Search Repository")
    @Test(description = "Validate if user is able to search repository.")
    @Description("Test Description: User should be able to search repository.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("1. Go to Repository List page. 2. Provide Repository name to be searched in search field and the resulst. ")
    public void searchRepository() {
        //Accessing link as static method throwing 500 Internal server error, with object, its working fine
        //Issue: MBLD2020-3005
        try {
            waitForRepositoryScanStatus();
            if (scanState) {
                waitTillRequestIsProcessed(1);
                /*1. To do: Get this value when user adds one or more repo during first sign up or from GET Repo API later
                 * 2. To Do.Already extracted reponame of newly added repo. Use it for search. */
                displayRepositoriesLinkInHeader().click();
                repositoryListPage.displayFirstRepoTile().click();
                waitTillRequestIsProcessed(4);
                //fluentWaitWithCustomTimeout("dashboard", 2);
                //To do: Repo name search case with camel casing fails. PR and Pr won't with below query. Remove substring() later
                String actualName = repositoryListPage.displayRepositoryNameOnSecondaryNavigation().getText();
                //Remove below useless logic for reponame after useless bug is fixed. MBLD2020-3096
                /*---------------------------------------------------------------------------------------*/
                String repoName = actualName.substring(1).toLowerCase();
                repoName = String.valueOf(actualName.charAt(0)).toUpperCase() + repoName;
                /*---------------------------------------------------------------------------------------*/
                repositoryListPage.displayRepositoryDashboardTab().click();
                displayRepositoriesLinkInHeader().click();
                fluentWaitWithCustomTimeout("firstRepoTile", 2);
                repositoryListPage.displayRepositorySearchField().clear();
                logger.info("Searching if repository exists: \"" + repoName + "\"");
                repositoryListPage.displayRepositorySearchField().sendKeys(repoName);
                repoTile = driver.findElement(By.xpath(".//span[contains(text(),'" + repoName + "')]"));
                if (repoTile.isDisplayed()) {
                    logger.info("Searched repository found.");
                    repoTile.click();
                    waitTillRequestIsProcessed(4);
                    //fluentWaitWithCustomTimeout("dashboard", 4);
                    repositoryListPage.displayRepositoryDashboardTab().click();
                    displayRepositoriesLinkInHeader().click();
                    fluentWaitWithCustomTimeout("firstRepoTile", 2);
                } else {
                    logger.warn("Seached repository not found.");
                }
            }
            scanState = false;
            displayRepositoriesLinkInHeader().click();
        } catch (AssertionError e) {
            logger.error("Validation for Search repository is failed. Check stacktrace for more info.", e);
        }
    }

    @Issues({@Issue("validateDashboardTabs")})
    @Story("Validate Repository Dashboard tabs")
    @Test(description = "Validate Repository Dashboard tabs.")
    @Description("Test Description: All tabs should be displayed and accessible after for successfully scanned repository.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("1. Go to Repository List page. 2. Search repository. 3. Click on repository tile displayed. 4. Validate if all tabs displayed." +
            "5. Validate if all labels displayed.")
    public void validateDashboardTabs() {
        /*To do: Create separate and independe method validations for below methods and move to individual test classes
         * 2. As of now execute all at once for demo purpose, but with single assertion.
         * 3. Add as many as assertions in each method.
         * 4. Assert labels with value from Translation.json*/
        try {
            waitForRepositoryScanStatus();
            if (scanState) {
                displayRepositoriesLinkInHeader().click();
                logger.info("Validating Tabs...");
                displayRepositoriesLinkInHeader().click();
                fluentWaitWithCustomTimeout("firstRepoTile", 2);
                repositoryListPage.displayFirstRepoTile().click();
                fluentWaitWithCustomTimeout("dashboard", 2);
                waitTillRequestIsProcessed(1);
                Assert.assertTrue(repositoryListPage.displayRepositoryDashboardTab().isDisplayed());
                Assert.assertTrue(repositoryListPage.displayBluePrintTab().isDisplayed());
                Assert.assertTrue(repositoryListPage.displayIssuesTab().isDisplayed());
                Assert.assertTrue(repositoryListPage.displayDuplicationTab().isDisplayed());
                Assert.assertTrue(repositoryListPage.displayComponentsTab().isDisplayed());
                Assert.assertTrue(repositoryListPage.displaySettingsTab().isDisplayed());
                logger.info("All tabs are displayed.");
                repositoryListPage.displayRepositoryDashboardTab().click();
                Assert.assertTrue(repositoryListPage.displayRepositoryDashboardTitle().isDisplayed());
                repositoryListPage.displayBluePrintTab().click();
                Assert.assertTrue(repositoryListPage.displayBluePrintTitle().isDisplayed());
                repositoryListPage.displayIssuesTab().click();
                Assert.assertTrue(repositoryListPage.displayIssuesTitle().isDisplayed());
                repositoryListPage.displayDuplicationTab().click();
                Assert.assertTrue(repositoryListPage.displayDuplicationTitle().isDisplayed());
                repositoryListPage.displayComponentsTab().click();
                Assert.assertTrue(repositoryListPage.displayComponentsTitle().isDisplayed());
                repositoryListPage.displaySettingsTab().click();
                Assert.assertTrue(repositoryListPage.displaySettingsTitle().isDisplayed());
                logger.info("All tabs titles are displayed.");
                waitTillRequestIsProcessed(1);
                displayRepositoriesLinkInHeader().click();
                fluentWaitWithCustomTimeout("firstRepoTile", 2);
            }

        } catch (AssertionError e) {
            logger.error("Validation for Dashboard tabs visibilty and labels is failed. Check stacktrace for more info.", e);
        }
        scanState = false;
    }
}