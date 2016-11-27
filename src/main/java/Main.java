import models.Environment;
import utils.ConfigReader;

import static spark.Spark.*;

/**
 * Created by manasb on 12-11-2016.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        final ConfigReader configReader = new ConfigReader();
        final Environment environment = configReader.getEnvironment();

        // If environment is DEV then static files are read from from an absolute path in order to
        // refresh static files.
        if (environment == Environment.DEV) {
            final String projectDir = System.getProperty("user.dir");
            final String staticDir = "\\src\\main\\resources\\public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }

        webSocket("/" + configReader.getWebSocketEndpoint(), ChatWebSocket.class);
        init();
    }

}
