package utilities;

import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class BuildChecker {

    private static final Logger logger = LogManager.getLogger(BuildChecker.class);
    private static final String EP = "https://api.github.com/repos/acellere/";
    private static final String EM = "ZDZiMGJhM2Q3MjQzMzFkODMyNTBjZDY1N2U5NmY1ZDdlNGUyNGMwMA==";
    public HashMap<String, String> commits;

    public BuildChecker() {
        commits = new HashMap<>();
    }

    private String getCommitID(String repoURL, String branchName) {
        String _dem = new String(Base64.getDecoder().decode(EM));
        String commitAPI = EP + repoURL + "/commits/" + branchName;
        Response response = given().auth().oauth2(_dem).
                when().get(commitAPI).
                then().extract().response();
        return response.jsonPath().get("sha").toString();
    }

    private boolean branchExists(String endpoint, String branchName) {
        String branchValue = null;
        try {
            String branchAPI = EP + endpoint + "/branches/" + branchName + "?per_page=100";
            String _dem = new String(Base64.getDecoder().decode(EM));
            Response response = given().auth().oauth2(_dem).
                    when().get(branchAPI).
                    then().extract().response();
            branchValue = response.jsonPath().getString("name");
            assert branchValue != null;
        } catch (AssertionError e) {
            logger.error("Branch " + "\"" + branchName + "\"" + " doesn't exist in " + endpoint +
                    ". Skipping build validation for: " + endpoint);
        }
        assert branchValue != null;
        return branchValue.contains(branchName);
    }

    public void getLatestCommitsFromGitHub(String branchName) {

        try {
            //To Do: Get branch name per repo and token from CLI
            String angularRepoName = "embold-ui";
            String nodeRepoName = "embold-backend";
            String gwsRepoName = "gws-ng";
            if (branchExists(angularRepoName, branchName)) {
                commits.put("angularscm", getCommitID(angularRepoName, branchName));
            } else {
                commits.put("angularscm", "Branch validation failed.");
            }
            if (branchExists(nodeRepoName, branchName)) {
                commits.put("nodescm", getCommitID(nodeRepoName, branchName));
            } else {
                commits.put("nodescm", "Branch validation failed.");
            }
            if (branchExists(gwsRepoName, branchName)) {
                commits.put("gwsscm", getCommitID(gwsRepoName, branchName));
            } else {
                commits.put("gwsscm", "Branch validation failed.");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            logger.warn("Unable to get GitHub commit.", e);
        }
    }

    //We don't have consistent response for all APIs provided to get deployed commit. Added custom parser.
    public void commitParser(String commitName, String endPoint, String appURL) throws IOException {
        URL url;
        BufferedReader br = null;
        try {
            String apiReq = appURL + endPoint;
            url = new URL(apiReq);
            URLConnection conn = url.openConnection();
            br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine = br.lines().collect(Collectors.joining());
            String[] splitString = inputLine.split("ID: ");
            String master = splitString[1];
            String target;
            /*--------------------Remove this unnecessary logic once getDeployedBuildAPI issue is fixed---------------------------
             * Response of api/buildVersion?component=gws API doesn't have space char before String 'Timestamp' and so modified like below --*/
            if (commitName.contains("gws")) {
                target = master.substring(master.lastIndexOf("T"));
            } else {
                target = master.substring(master.lastIndexOf("T") - 1);
            }
            /*------------------------------------------------------------------------*/
            String commitID = StringUtils.replace(master, target, "").trim();
            commits.put(commitName, commitID);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("Unable to get deployed commit.", e);
        } finally {
            assert br != null;
            br.close();
        }
    }

    public HashMap<String, String> getCommits(String branchName, String appURL) throws IOException {
        commitParser("angulardeployed", "buildVersion", appURL);
        commitParser("nodedeployed", "api/buildVersion", appURL);
        commitParser("gwsdeployed", "api/buildVersion?component=gws", appURL);
        getLatestCommitsFromGitHub(branchName);
        return commits;
    }
}
