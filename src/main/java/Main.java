import models.Environment;
import utils.ConfigReader;

import static spark.Spark.*;

/**
 * Created by manasb on 12-11-2016.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        ConfigReader configReader = new ConfigReader();
        Environment environment = configReader.getEnvironment();

        // If environment is DEV then static files are read from from an absolute path in order to
        // refresh static files.
        if (environment == Environment.DEV) {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "\\src\\main\\resources\\public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }

        webSocket("/chat", ChatWebSocket.class);
        init();
    }

}
