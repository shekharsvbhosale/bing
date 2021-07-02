package pages.commons;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

public class ToastNotifications extends Elemental {
    private static final Logger logger = LogManager.getLogger(ToastNotifications.class);

    public static WebElement displaySuccessToastNotification() {

        return fluentWaitWithCustomTimeout("success_toast", 45);
    }

    public static WebElement displayCloseButtonOnToaster() {
        return getWebElement("closeButton_ToastNotification");
    }
}
