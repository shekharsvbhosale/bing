package github;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.github.GitHubSignInPage;
import pages.github.InstallEmboldAppOnGitHubPage;
import pages.repositorylist.RepositoryListPage;
import pages.signin.EmboldSignInPage;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Install Embold App on GitHub")
public class InstallEmboldAppOnGitHubTests extends Elemental {
    InstallEmboldAppOnGitHubPage appOnGitHub;
    EmboldSignInPage emboldSignInPage;
    GitHubSignInPage gitHubSignInPage;
    RepositoryListPage repositoryListPage;

    InstallEmboldAppOnGitHubTests() {
        super();
        appOnGitHub = new InstallEmboldAppOnGitHubPage();
        emboldSignInPage = new EmboldSignInPage();
        gitHubSignInPage = new GitHubSignInPage();
        repositoryListPage = new RepositoryListPage();
    }

    @Story("Install Embold App on GitHub validations")
    @Test(description = "Validate installing Embold App on GitHub.")
    @Description("Test Description: Validate installing Embold App on GitHub.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Validate installing Embold App on GitHub..")
    public void authorizeEmboldApp() {
        gitHubSignInPage.signInToGitHub(getPropertyValue("ghuser"), getPropertyValue("ghpass"));
        if (driver.getCurrentUrl().contains("login/oauth/authorize")) {
            Assert.assertTrue(appOnGitHub.displayEmboldOrgName().isDisplayed());
            Assert.assertTrue(appOnGitHub.displayVerifyIdentityText().isDisplayed());
            Assert.assertTrue(InstallEmboldAppOnGitHubPage.displayAuthorizeOrgButton().isDisplayed());
            InstallEmboldAppOnGitHubPage.displayAuthorizeOrgButton().click();
        }
    }
}
