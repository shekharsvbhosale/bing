package utilities;

import java.util.LinkedHashMap;

public class OAuthHandler {

    public static LinkedHashMap<String, String> sigCodes = new LinkedHashMap<>();

    public static void setGitHubAccessCode(String URI, String codeName) {
        String ghcode = URI.substring(URI.indexOf("=") + "=".length());
        sigCodes.put(codeName, ghcode);
    }

    public static String getGitHubAccessCode(String codeName) {
        return sigCodes.get(codeName);
    }

    public static String getEmboldAccessToken(String token) {
        return sigCodes.get(token);
    }

    public void setEmboldAccessToken(String token) {
        APIInvoker apiInvoker = new APIInvoker();
        sigCodes.put(token, apiInvoker.generateEmboldAccessToken(token, sigCodes.get("ghcode")));
    }
}
