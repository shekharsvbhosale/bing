package pages.commons;

import core.Elemental;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class PrimaryHeader extends Elemental {
    public static WebElement displayRepositoriesLinkInHeader() throws NoSuchElementException {
        return getWebElement("repositories_link");
    }

    public WebElement displayUserAvatarInHeader() throws NoSuchElementException {
        return getClickableElement("loggedInUserAvatar", 3);
    }

    public boolean verifydisplayRepositoriesLinkInHeader() {
        return displayRepositoriesLinkInHeader().isDisplayed();
    }

}
