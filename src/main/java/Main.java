import models.Environment;
import org.eclipse.jetty.websocket.api.Session;
import utils.ConfigReader;
import utils.HttpResponseFactory;

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

        port(configReader.getPort());

        webSocket("/" + configReader.getWebSocketEndpoint(), ChatWebSocket.class);
        post("/edit-username", (request, response) -> {
            String[] bodySplit = request.body().split(",");

            if (bodySplit.length != 2) {
                return HttpResponseFactory.createBadRequestResponse("Body must be in the format: existing_username,new_username");
            }

            try {
                ChatController chatController = ChatController.getInstance();
                final Session session = chatController.updateUsername(bodySplit[0], bodySplit[1]);
                chatController.sendIdentityToSession(session);
                chatController.broadcastConnectedUsers();
                return HttpResponseFactory.createOkResponse(null);
            } catch (Exception e) {
                return HttpResponseFactory.createBadRequestResponse(e.getMessage());
            }
        });
    }

    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

}
