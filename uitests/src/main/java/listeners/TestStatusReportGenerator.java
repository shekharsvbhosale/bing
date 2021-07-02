package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.util.List;
import java.util.Set;

public class TestStatusReportGenerator implements IReporter {
    private static final Logger logger = LogManager.getLogger(TestStatusReportGenerator.class);

    @Override
    public void generateReport(
            List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        for (ISuite suite : suites) {
            generateReport(suite);
        }
    }

    private void generateReport(ISuite suite) {
        for (ISuiteResult each : suite.getResults().values()) {
            generateReport(each);
        }
    }

    private void generateReport(ISuiteResult result) {
        generateReport("Failed Tests", result.getTestContext().getFailedTests().getAllResults());
        generateReport("Passed Tests", result.getTestContext().getPassedTests().getAllResults());
        generateReport("Skipped Tests", result.getTestContext().getSkippedTests().getAllResults());
    }

    private void generateReport(String msg, Set<ITestResult> results) {
        if (results.isEmpty()) {
            logger.error("No  " + msg);
            return;
        }
        logger.error("Adding to logger::: " + msg);
        for (ITestResult result : results) {
            generateReport(result);
        }
    }

    private void generateReport(ITestResult result) {
        logger.error("Method name " + result.getMethod().getMethodName());
        logger.error("Invocation " + result.getMethod().getCurrentInvocationCount());
    }
}
