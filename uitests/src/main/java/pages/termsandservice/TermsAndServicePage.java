package pages.termsandservice;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

public class TermsAndServicePage extends Elemental {

    private static final Logger logger = LogManager.getLogger(TermsAndServicePage.class);

    public WebElement displayEmboldLogo() {
        return getWebElement("terms_Service_embold_logo");
    }

    public WebElement displayTermsAndServiceLabel() {
        return getWebElement("terms_Service_label");
    }

    public WebElement displayServiceIllustration() {
        return getWebElement("terms_service_illustration");
    }

    public WebElement displayConsentCheckbox() {
        return getClickableElement("terms_Service_checkbox", 3);
    }

    public WebElement displayConsentText() {
        return getWebElement("terms_Service_AgreeText");
    }

    public WebElement displayConsentLink() {
        return getClickableElement("terms_Service_link", 3);
    }

    public WebElement displaySubmitButton() {
        return getClickableElement("terms_Service_NextButton", 3);
    }

    public WebElement displayTermsAndServiceDescription() {
        return getWebElement("terms_Service_description");
    }

    public void acceptTermsAndService() {
        waitTillRequestIsProcessed(2);
        displayConsentCheckbox().click();
        displaySubmitButton().click();
        logger.info("Terms of service accepted.");
        getClickableElement("firstAddOrg_button", 3);
        if (!ghSignInState) {
            ghSignInState = true;
        }
    }

    public boolean verifyConsentPageURL() {
        return driver.getCurrentUrl().contains("auth/consent/");
    }
}
