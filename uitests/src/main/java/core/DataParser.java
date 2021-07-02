package core;

import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataParser {
    public FileInputStream stream;
    public String locatorFile;
    public Properties propFile = new Properties();

    public DataParser(String fileName) throws IOException {
        this.locatorFile = fileName;
        stream = new FileInputStream(locatorFile);
        propFile.load(stream);
    }

    public By getElementLocator(String locatorLabel) {
        String locatorProperty = propFile.getProperty(locatorLabel);
        String locatorType = locatorProperty.split("!")[0];
        String locatorValue = locatorProperty.split("!")[1];
        By locator;

        switch (locatorType) {
            case "id":
                locator = By.id(locatorValue);
                break;
            case "tagName":
                locator = By.tagName(locatorValue);
                break;
            case "linkText":
                locator = By.linkText(locatorValue);
                break;
            case "partialLink":
                locator = By.partialLinkText(locatorValue);
                break;
            case "css":
                locator = By.cssSelector(locatorValue);
                break;
            case "name":
                locator = By.name(locatorValue);
                break;
            case "xpath":
                locator = By.xpath(locatorValue);
                break;
            default:
                locator = null;
                break;
        }
        return locator;
    }

    public String getSingularProperty(String propKey) {
        return propFile.getProperty(propKey);
    }
}
