package administration.onboarding;

import core.Elemental;
import io.qameta.allure.*;
import listeners.TestReportListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.administration.onboarding.OnboardingForNewOrganizationPage;

import java.util.NoSuchElementException;

@Listeners({TestReportListener.class})
@Epic("Regression Tests")
@Feature("Add new organization")
public class OnboardingForNewOrganizationTests extends Elemental {
    OnboardingForNewOrganizationPage organizationPage;

    OnboardingForNewOrganizationTests() {
        super();
        organizationPage = new OnboardingForNewOrganizationPage();
    }

    @Issues({@Issue("LoggedInUserSCMAccountIsDisplayed")})
    @Story("Add new organization to Embold validations")
    @Test(description = "Validate if list of organization(s) is displayed for selected account.")
    @Description("Test Description: Validate if list of organization(s) is displayed for selected account.")
    @Severity(SeverityLevel.NORMAL)
    @Step("Validate if list of organization(s) is displayed for selected account.")
    public boolean LoggedInUserSCMAccountIsDisplayed() {
        String accName = getPropertyValue("ghuser");
        try {
            Assert.assertEquals(organizationPage.displayLoggedInUserSCMAccountName().getText(), accName);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Unable to find org name.");
        }
        return true;
    }

    @Issues({@Issue("AddOrganizationToEmbold")})
    @Story("Add new organization to Embold validations")
    @Test(description = "Validate if user can add new organization to Embold.")
    @Description("Test Description: Validate if user can add new organization to Embold.")
    @Severity(SeverityLevel.NORMAL)
    @Step("Validate if user can add new organization to Embold.")
    public void AddOrganizationToEmbold() {
        try {
            if (LoggedInUserSCMAccountIsDisplayed())
                organizationPage.displayAddOrganizationButton().click();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Unable to add org.");
        }
    }
}
