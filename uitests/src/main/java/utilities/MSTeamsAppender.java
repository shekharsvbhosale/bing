/*This code is referenced from gws-ng implemented by Sud*/
package utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;

@Plugin(
        name = "Autologs",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE
)
public class MSTeamsAppender extends AbstractAppender {

    private static final ObjectMapper mapper = new ObjectMapper();

    //Provide teamsWebhook via CLI else set Env var. If not set/provided, MSTeams logging will be disabled..
    private static final String ENV_WEBHOOK = "teamsWebhook";
    private static String webhookUrl;

    static {
        if (System.getProperty(ENV_WEBHOOK) != null) {
            webhookUrl = System.getProperty(ENV_WEBHOOK);
        } else {
            if (System.getenv(ENV_WEBHOOK) != null) {
                webhookUrl = System.getenv(ENV_WEBHOOK);
            }
        }
    }

    @Deprecated
    protected MSTeamsAppender(String name, Filter filter) {
        super(name, filter, null);
        if (StringUtils.isEmpty(webhookUrl)) {
            error("****** No Teams webhook configured. Teams logging will be disabled ******");
        }
    }

    @PluginFactory
    public static MSTeamsAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter
    ) {
        return new MSTeamsAppender(name, filter);
    }

    private static String serializeObjectToString(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    @Override
    public void append(LogEvent event) {
        if (StringUtils.isEmpty(webhookUrl)) {
            return;
        }

        List<Object> sections = new ArrayList<>();
        List<Fact> facts = new ArrayList<>();
        //Get name of class under execution to know exact component from where logs are generated.
        String currentComponent = event.getLoggerName();
        facts.add(new Fact("Environment", System.getProperty("profileName").toUpperCase()));
        facts.add(new Fact("Component", currentComponent));
        //Get Current systime from where Test Execution is triggered
        facts.add(new Fact("Time", ZonedDateTime.now().toString()));
        //To Do: Get Report Link from Maven profile. Using hardcoded temporarily
        facts.add(new Fact("Test Report", "[**View Report**](http://192.168.2.24:7744/embold/v2/test-automation/signin)"));
        sections.add(new Section(event.getMessage().getFormattedMessage(), facts, true));
        Message msg = new Message("MessageCard", "http://schema.org/extensions", "FF0000",
                event.getMessage().getFormattedMessage(), sections);

        try {
            String text = serializeObjectToString(msg);
            postWebhook(text);
        } catch (JsonProcessingException e) {
            error("Error while posting log msg to teams queue", e);
        }
    }

    public void postWebhook(String msg) {
        Response response = given()
                .contentType(ContentType.JSON).
                        with()
                .body(msg).
                        when().post(webhookUrl);
        //Check if provided webhook is valid or not.
        if (response.getStatusCode() != 200)
            error("::::::Invalid webhook URL::::::");
        //To do: Implement rate limiter to avoid status 429
        if (response.getBody().asString().contains("429"))
            error("::::::Rate limit is reached for given webhook. Please try again after sometime.");
    }

    class Message {
        @JsonProperty("@type")
        private final String type;
        @JsonProperty("@context")
        private final String context;
        @JsonProperty("themeColor")
        private final String themeColor;
        @JsonProperty("summary")
        private final String summary;
        @JsonProperty("sections")
        private final List<Object> sections;

        public Message(String type, String context, String themeColor, String summary, List<Object> sections) {
            this.type = type;
            this.context = context;
            this.themeColor = themeColor;
            this.summary = summary;
            this.sections = sections;
        }
    }

    class Section {
        @JsonProperty("activityTitle")
        private final String activityTitle;
        @JsonProperty("facts")
        private final List<Fact> facts;
        @JsonProperty("markdown")
        private final boolean markdown;

        public Section(String activityTitle, List<Fact> facts, boolean markdown) {
            this.activityTitle = activityTitle;
            this.facts = facts;
            this.markdown = markdown;
        }
    }

    class Fact {
        @JsonProperty("name")
        private final String name;
        @JsonProperty("value")
        private final String value;

        public Fact(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
