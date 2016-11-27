import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 * Created by manasb on 12-11-2016.
 */

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Environment type must be provided");
        }

        Environment environment = Environment.valueOf(args[0]);

        // If environment is DEV then static files are read from from an absolute path in order to
        // refresh static files.
        if (environment == Environment.DEV) {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "\\src\\main\\resources\\public";
            staticFiles.externalLocation(projectDir + staticDir);
            log.info(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }

        webSocket("/chat", ChatWebSocket.class);
        init();
    }

    public enum Environment {
        DEV, PROD
    }

}
