package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class Elemental {

    private static final Logger logger = LogManager.getLogger(Elemental.class.getName());

    public static WebDriver driver;
    public static String browserName;
    public static DataParser locatorParser;
    public static LoadEnvProps loadEnvProps;
    public static Capabilities caps;
    public static String embURL = null;
    public static boolean ghSignInState;
    public static boolean bbSignInState = false;
    public static List<String> listValues;

    //To to: Implement OS independent paths later. For ex, getCanonical methods.
    static {
        try {
            locatorParser = new DataParser("./src/main/resources/props/Locators.properties");
            loadEnvProps = new LoadEnvProps();
        } catch (IOException e) {
            logger.error("Locatore resource file not found. Terminating execution.");
            e.getStackTrace();
            System.exit(-1);
        }
    }

    public WebElement element;

    public static WebElement getClickableElement(String locatorKey, int waitPeriod) {
        By element = locatorParser.getElementLocator(locatorKey);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitPeriod));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return driver.findElement(element);
    }

    public static void fluentWaitForWebElement(String locatorKey) throws TimeoutException {
        By waitElement = locatorParser.getElementLocator(locatorKey);
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(6))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class, TimeoutException.class);
        WebElement ele = fluentWait.until(driver -> driver.findElement(waitElement));
    }

    public static WebElement fluentWaitWithCustomTimeout(String locatorKey, int timeout) throws TimeoutException {
        By waitElement = locatorParser.getElementLocator(locatorKey);
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class, TimeoutException.class);
        return fluentWait.until(driver -> driver.findElement(waitElement));
    }


    public static boolean checkIfElementPresent(String locatorKey) {
        By waitElement = locatorParser.getElementLocator(locatorKey);
        return driver.findElement(waitElement).isDisplayed();
    }

    //To do: Will be removed after implementation of Selenium waits and Thread synchronozation.
    //Execution time will be reduced approximately by 50%
    public static void waitTillRequestIsProcessed(int period) {

        try {
            Thread.sleep(period * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static WebElement getWebElement(String locatorKey) {
        return driver.findElement(locatorParser.
                getElementLocator(locatorKey));
    }

    public static String getLanguageOfBrowser() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return window.navigator.language");
    }

    public List<WebElement> getElementsList(String locatorKey) {
        List<WebElement> list;
        By waitElement = locatorParser.getElementLocator(locatorKey);
        list = driver.findElements(waitElement);
        return list;
    }

    /*To Do:Use maven dependency to create driver*/
    public String getBrowserName(String browser) {
        browserName = browser;
        return browserName;
    }

    public void setWebDriver() {
        String browser = getBrowserName(browserName);
        if (browser.equalsIgnoreCase("chrome")) {
            logger.info("Execution is started on Google Chrome");
            System.setProperty("webdriver.chrome.driver", "./src/main/resources/executables/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            //WebDriverManager.chromedriver().setup();
            //*for devtools option, pass below arg*//*
            //options.addArguments("--auto-open-devtools-for-tabs");
            options.addArguments("start-maximized");
            options.addArguments("--disable-gpu");
            options.addArguments("enable-automation");
            //*Avoid using headless mode. Some tests are failing in headless mode.*//*
            //To Do: Make configurable with Maven Profile
            //options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-browser-side-navigation");
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);
            options.addArguments("--test-type");
            options.addArguments("--disable-popup-blocking");
            driver = new ChromeDriver(options);
            getLanguageOfBrowser();
        } else if (browser.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", "./src/main/resources/executables/geckodriver.exe");
            //WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        } else if (browser.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "./src/main/resources/executables/msedgedriver.exe");
            //WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            driver.manage().window().maximize();
        }
    }

    public void openPlatform(String appURL) {
        embURL = appURL;
        driver.get(embURL);
        waitTillWholePageIsLoaded();
    }

    public String getPropertyValue(String key) {
        return (String) loadEnvProps.loadPropertiesFile("appConfig.properties").get(key);
    }

    public void waitTillWholePageIsLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public void tearDown() {
        try {
            caps = ((RemoteWebDriver) driver).getCapabilities();
            //To DO: Generate All Env Props for Firefox and MS Edge
            createEnvironmentProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.manage().deleteAllCookies();
        driver.quit();
    }

    public void createEnvironmentProperties() throws IOException {

        File envProps = new File(".//allure-results//environment.properties");
        if (envProps.exists() && !envProps.isDirectory()) {
            envProps.createNewFile();
        }
        FileOutputStream propFile = new FileOutputStream(envProps, false);
        try {
            Properties prop = new Properties();
            prop.setProperty("Embold URL", embURL);
            prop.setProperty("Platform", System.getProperty("os.name"));
            prop.setProperty("BrowserName", String.valueOf(caps.getCapability("browserName")));
            prop.setProperty("BrowserVersion", String.valueOf(caps.getCapability("browserVersion")));
            prop.setProperty("ScreenResolution", String.valueOf(driver.manage().window().getSize()));
            prop.setProperty("ExecutedBy", System.getProperty("user.name"));
            prop.store(propFile, "");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}