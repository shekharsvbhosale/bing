package utilities;

import core.Elemental;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class APIInvoker extends Elemental {

    private static final Logger logger = LogManager.getLogger(APIInvoker.class);
    public static String TRANSLATIONJSON_ENDPOINT;

    public static String setTranslationJSON() {
        String lang = getLanguageOfBrowser();
        switch (lang) {
            case "en":
            case "en-US":
            case "en-GB":
                TRANSLATIONJSON_ENDPOINT = "/assets/i18n/en.json";
                break;
            case "fr":
                TRANSLATIONJSON_ENDPOINT = "/assets/i18n/fr.json";
                break;
            case "de":
                TRANSLATIONJSON_ENDPOINT = "/assets/i18n/de.json";
                break;
            default:
                logger.error("No Translation JSON available for given language.");
        }
        return TRANSLATIONJSON_ENDPOINT;
    }

    public Response consumeGHCode(String scmcode) {
        RestAssured.defaultParser = Parser.JSON;
        String _platform = getPropertyValue("appUrl");
        return
                given().queryParam("code", scmcode).headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().post(_platform + "api/v2/auth/gh").
                        then().contentType(ContentType.JSON).extract().response();
    }

    public String generateEmboldAccessToken(String tokenKey, String codeKey) {
        Response response = consumeGHCode(codeKey);
        logger.info("Check::: " + response.jsonPath().get(tokenKey).toString());
        return response.jsonPath().get(tokenKey).toString();
    }

    private Response getAPIRequestURL(String baseURI) {
        RestAssured.defaultParser = Parser.JSON;
        return
                given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().get(baseURI + TRANSLATIONJSON_ENDPOINT).
                        then().contentType(ContentType.JSON).extract().response();
    }

    public String getSignInPageLabelsFromTranslationJSON(String labelKey) {
        Response response = getAPIRequestURL(getPropertyValue("appUrl"));
        return response.jsonPath().getMap("signup").get(labelKey).toString();
    }

    public String getRepositoryListLabelsFromTranslationJSON(String labelKey) {
        Response response = given().
                when().get(getPropertyValue("appUrl") + setTranslationJSON()).
                then().contentType(ContentType.fromContentType("application/json")).extract().response();
        return response.jsonPath().getMap("repository").get(labelKey).toString();
    }
}
