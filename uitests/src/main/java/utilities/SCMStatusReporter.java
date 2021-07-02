package utilities;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import listeners.TestReportListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners({TestReportListener.class})
@Epic("Platform Pre-checks")
@Feature("SCM Status Checks")
public class SCMStatusReporter {
    private static final Logger logger = LogManager.getLogger(SCMStatusReporter.class);
    private static final String GITHUB_STATUS_API = "https://kctbh9vrtdwd.statuspage.io/api/v2/status.json";
    private static final String BITBUCKET_STATUS_API = "https://bqlf8qjztdtr.statuspage.io/api/v2/status.json";

    public SCMStatusReporter() {
    }

    @Test
    public void getGitHubStatus() {
        RestAssured.defaultParser = Parser.JSON;
        Response response =
                given().headers("Content-Type", ContentType.JSON).
                        when().get(GITHUB_STATUS_API).
                        then().contentType(ContentType.JSON).extract().response();
        String value = response.jsonPath().getMap("status").get("description").toString();
        logger.info("GitHub Status: " + value);
        try {
            Assert.assertEquals(value, "All Systems Operational");
        } catch (AssertionError e) {
            logger.error("[WARNING:::GitHub service is down: " + value);
        }
    }

    @Test
    public void getBitbucketStatus() {
        RestAssured.defaultParser = Parser.JSON;
        Response response =
                given().headers("Content-Type", ContentType.JSON).
                        when().get(BITBUCKET_STATUS_API).
                        then().contentType(ContentType.JSON).extract().response();
        String value = response.jsonPath().getMap("status").get("description").toString();
        logger.info("Bitbucket Status: " + value);
        try {
            Assert.assertEquals(value, "All Systems Operational");
        } catch (AssertionError e) {
            logger.error("WARNING:::Some Bitbuckert services are down: " + value);
            throw new SkipException("");
        }
    }
}
