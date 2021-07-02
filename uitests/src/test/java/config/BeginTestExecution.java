package config;

import core.Elemental;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import utilities.APIInvoker;

public class BeginTestExecution extends Elemental {

    private static final Logger logger = LogManager.getLogger(BeginTestExecution.class);


    @BeforeSuite
    public void StartExecution() {
        getBrowserName(getPropertyValue("browser"));
        setWebDriver();
        logger.info("Detecting browser language...");
        logger.info("Browser Language is: " + getLanguageOfBrowser());
        logger.info("Opening Embold...");
        String instance = getPropertyValue("appUrl");
        openPlatform(instance);
        logger.info("Embold Platform opened: " + instance);
        logger.info("Loading translation JSON: " + APIInvoker.setTranslationJSON());
    }
}