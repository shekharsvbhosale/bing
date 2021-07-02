package pages.addrepositories;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.administration.onboarding.OnboardingForNewOrganizationPage;

import java.util.LinkedList;
import java.util.List;

public class AddRepositoriesPage extends Elemental {

    public static final List<String> repoNames = new LinkedList<>();
    private static final Logger logger = LogManager.getLogger(AddRepositoriesPage.class);

    public WebElement skipToNextButton() {
        return getClickableElement("skipToNext_button", 3);
    }

    public boolean verifyAddRepositoriesPageURL() {
        return driver.getCurrentUrl().contains("/repositories/link");
    }

    public boolean verifyLinkRepoContainer() {
        return fluentWaitWithCustomTimeout("linkRepoContainer", 4).isDisplayed();
    }

    public WebElement displaySelectRepositoryCheckbox() {
        WebElement name = driver.findElement(By.xpath(".//span[contains(.,'" + repoNames.get(0) + "')]"));
        return name;
    }

    public WebElement displayLinkRepositoriesIllustration() {
        return fluentWaitWithCustomTimeout("linkRepsitories_illustration", 6);
    }

    public List<WebElement> getListOfNodAddedRepositories() {
        getElementsList("repositoryList_labels");
        return getElementsList("repositoryList_labels");
    }

    public void setRepositoryName() {
        String firstRepo = getListOfNodAddedRepositories().get(0).getText();
        repoNames.add(firstRepo);
    }

    public WebElement displaySearchRepositoriesField() {
        return fluentWaitWithCustomTimeout("searchRepositoriesAddRepoPage_field", 4);
    }

    public WebElement displayAddRepositoriesToEmboldButton() {
        return fluentWaitWithCustomTimeout("addRepositories_button", 2);
    }

    public boolean searchRepositoryToBeAdded() {
        //Add first repo temporarily
        displaySearchRepositoriesField().sendKeys(repoNames.get(0));
        return displaySelectRepositoryCheckbox().getText().equals(repoNames.get(0));
    }

    //To do: Move this to AddRepositoriesTests class
    public void addGitHubRepositoriesToEmbold() {
        boolean listDisplayed = fluentWaitWithCustomTimeout("searchRepositoriesAddRepoPage_field", 2).isDisplayed();
        logger.info("Repo list displayed?: " + listDisplayed);
        if (listDisplayed) {
            displaySelectRepositoryCheckbox().click();
            displayAddRepositoriesToEmboldButton().click();
            fluentWaitWithCustomTimeout("repositoryTile", 4);
            logger.info("Repository added successfully.");
            repoNames.clear();
        }
        if (!ghSignInState) {
            ghSignInState = true;
        }
    }

    //To do: Move this to AddRepositoriesTests class
    public void addBitbucketRepositoriesToEmbold(OnboardingForNewOrganizationPage organizationPage) {
        boolean listDisplayed = fluentWaitWithCustomTimeout("addRepositories_button", 2).isDisplayed();
        logger.info("Repo list displayed?: " + listDisplayed);
        if (listDisplayed) {
            //Searching just to verify if search is working. Not using any search results though.
            displaySearchRepositoriesField().sendKeys(repoNames.get(0));
            //displaySelectRepositoryCheckbox().click();
            fluentWaitWithCustomTimeout("selectFirstRepository_checkbox", 2).click();
            displayAddRepositoriesToEmboldButton().click();
            organizationPage.displaySkipToNextButton().click();
        }
        fluentWaitWithCustomTimeout("firstRepoTile", 4);
        logger.info("Repository added successfully.");
        repoNames.clear();
        if (!bbSignInState) {
            bbSignInState = true;
        }
    }
}
