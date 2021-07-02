package listeners;

import core.Elemental;
import io.qameta.allure.Attachment;
import io.qameta.allure.Issues;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class TestReportListener extends Elemental implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestReportListener.class);

    private static String getMethodNameUnderExecution(ITestResult iTestResult) {
        return iTestResult.getTestClass().getName() + "." + iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String storeTextExecutionLogs(String logs) {
        //To Do: Add logs here
        return logs;
    }

    @Attachment(value = "Test screenshot", type = "image/png")
    public byte[] saveScreenshotAsPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {

        logger.info("Validating: " + getMethodNameUnderExecution(iTestResult) + " ");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if (driver != null) {
            logger.error("Validation for "
                    + getMethodNameUnderExecution(iTestResult) + " has failed. Refer attached screenshot!");
            saveScreenshotAsPNG(driver);
        }
        storeTextExecutionLogs("Validation for "
                + getMethodNameUnderExecution(iTestResult) + " has failed.");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
        if (method != null && method.isAnnotationPresent(Issues.class)
                && Arrays.asList(method.getAnnotation(Issues.class).value()).isEmpty()) {
            ArrayList<String> issues = new ArrayList<>();
            Arrays.asList(method.getAnnotation(Issues.class).value()).forEach(issue -> issues.add(issue.value()));
            throw new SkipException(String.format("Execution Condition failure::: %s ::: Skipping this test", issues));
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

        //To do: Get success percentatge using Allure history and implement
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
