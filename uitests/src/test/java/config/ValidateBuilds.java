package config;

import core.Elemental;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utilities.BuildChecker;

import java.io.IOException;
import java.util.HashMap;

@Listeners({TestReportListener.class})
@Epic("Platform Pre-checks")
@Feature("Embold Build Checks")
public class ValidateBuilds extends Elemental {

    private static final Logger logger = LogManager.getLogger(ValidateBuilds.class);
    BuildChecker buildChecker;
    HashMap<String, String> commitsMap;

    @Test
    @Parameters("branch")
    public void getCommitInfo(String branch) throws IOException {
        assert branch != null;
        String appUrl = getPropertyValue("appUrl");
        buildChecker = new BuildChecker();
        commitsMap = buildChecker.getCommits(branch, appUrl);
        //try {
        //Add logic to get current branch from gammatestautomation repository
        if (branch.isEmpty() || branch.equalsIgnoreCase("${branch}")) {
            throw new SkipException("No branch provided. Skipping build checks.");
        }
        verifyDeployedAngularBuild();
        verifyDeployedNodeBuild();
        verifyDeployedGWSBuild();
    }

    public void verifyDeployedAngularBuild() {
        try {
            logger.info("Validating Angular build...");
            Assert.assertEquals(commitsMap.get("angulardeployed"), commitsMap.get("angularscm"));
            logger.info("Deployed \"embold-ui\" is up to date.");
        } catch (AssertionError aio) {
            logger.error("Deployed \"embold-ui\" is not up to date.");
            logger.error("ERROR::: [embold-ui-deployed: " + commitsMap.get("angulardeployed")
                    + ", embold-ui-scm: " + commitsMap.get("angularscm") + "]");
        }
    }

    public void verifyDeployedNodeBuild() {
        try {
            logger.info("Validating Node build...");
            Assert.assertEquals(commitsMap.get("nodedeployed"), commitsMap.get("nodescm"));
            logger.info("Deployed \"embold-backend\" is up to date.");
        } catch (AssertionError aio) {
            logger.error("Deployed \"embold-backend\" is not up to date.");
            logger.error("ERROR::: [embold-backend-deployed: " + commitsMap.get("nodedeployed")
                    + ", embold-backend-scm:: " + commitsMap.get("nodescm") + "]");
        }
    }

    public void verifyDeployedGWSBuild() {
        try {
            logger.info("Validating GWS build...");
            Assert.assertEquals(commitsMap.get("gwsdeployed"), commitsMap.get("gwsscm"));
            logger.info("Deployed \"gws-ng\" is up to date.");
        } catch (AssertionError aio) {
            logger.error("Deployed \"gws-ng\" is not up to date.");
            logger.error("ERROR::: [gws-ng-deployed: " + commitsMap.get("gwsdeployed")
                    + ", gws-ng-scm: " + commitsMap.get("gwsscm") + "]");
        }
    }

}
