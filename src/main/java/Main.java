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
        final ChatService chatService = ChatService.getInstance();

        setupStaticFilesLocation(environment);
        port(configReader.getPort());
        setupWebSocket(configReader.getWebSocketEndpoint());
        setupEndpoints(chatService);
    }

    private static void setupStaticFilesLocation(final Environment environment) {
        // If environment is DEV then static files are read from from an absolute path in order to
        // refresh static files.
        if (environment == Environment.DEV) {
            final String projectDir = System.getProperty("user.dir");
            final String staticDir = "\\src\\main\\resources\\public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }
    }

    private static void setupWebSocket(String endpoint) {
        webSocket("/" + endpoint, ChatWebSocket.class);
    }

    private static void setupEndpoints(final ChatService chatService) {
        post("/edit-username", (request, response) -> {
            final String[] bodySplit = request.body().split(",");

            if (bodySplit.length != 2) {
                return HttpResponseFactory.createBadRequestResponse("Body must be in the format: " +
                        "existing_username,new_username");
            }

            try {
                final Session session = chatService.updateUsername(bodySplit[0], bodySplit[1]);
                chatService.sendIdentityToSession(session);
                chatService.broadcastConnectedUsers();
                return HttpResponseFactory.createOkResponse(null);
            } catch (Exception e) {
                return HttpResponseFactory.createBadRequestResponse(e.getMessage());
            }
        });
    }
}
