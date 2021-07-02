package pages.repositorylist;

import core.Elemental;
import org.openqa.selenium.WebElement;

public class RepositoryListPage extends Elemental {
    public static boolean verifyRepositoryListPageURL() {
        return driver.getCurrentUrl().contains("/repositories?offset");
    }

    public static boolean verifyIfRepositoryListPage() {
        return fluentWaitWithCustomTimeout("repositoryListSectionHeading", 10).isDisplayed();
    }

    public static boolean displayRepositoryTile() {
        return fluentWaitWithCustomTimeout("firstRepoTile", 4).isDisplayed();
    }

    public WebElement displayEmboldLogoOnRepositoryListPage() {
        return getClickableElement("embold_logo_RLPage", 2);
    }

    public WebElement displayRepositorySearchField() {
        return fluentWaitWithCustomTimeout("search_repository", 45);
    }

    public boolean displayFilterCard() {
        //getClickableElement("filterCard", 35);
        return fluentWaitWithCustomTimeout("filterCard", 6).isDisplayed();
    }

    public WebElement displayRepositoryDashboardTitle() {
        return getWebElement("dashboard");
    }

    public WebElement displayRepositoryDashboardTab() {
        return getWebElement("dashboard_tab");
    }

    public WebElement displayFirstRepoTile() {
        return getClickableElement("firstRepoTile", 2);
    }

    public WebElement displayRepositoryNameOnSecondaryNavigation() {
        return getWebElement("repositoryName");
    }

    public WebElement displayBluePrintTitle() {
        return getWebElement("blue_print");
    }

    public WebElement displayBluePrintTab() {
        return getWebElement("blue_print_tab");
    }

    public WebElement displayPullRequestsTitle() {
        return getWebElement("pull_request");
    }

    public WebElement displayPullRequestsTab() {
        return getWebElement("pull_request_tab");
    }

    public WebElement displayIssuesTitle() {
        return getWebElement("issues");
    }

    public WebElement displayIssuesTab() {
        return getWebElement("issues_tab");
    }

    public WebElement displayDuplicationTitle() {
        return getWebElement("duplication");
    }

    public WebElement displayDuplicationTab() {
        return getWebElement("duplication_tab");
    }

    public WebElement displayComponentsTitle() {
        return getWebElement("components");
    }

    public WebElement displayComponentsTab() {
        return getWebElement("components_tab");
    }

    public WebElement displaySettingsTitle() {
        return getWebElement("settings_integrations");
    }

    public WebElement displaySettingsTab() {
        return getWebElement("settings_integrations_tab");
    }

    public WebElement displayNoSearchResultsText() {
        return getWebElement("noResults_text");
    }

}
