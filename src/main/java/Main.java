import org.eclipse.jetty.websocket.api.Session;
import services.ChatService;
import services.ConfigService;
import utils.HttpResponseFactory;

import static spark.Spark.*;

/**
 * Created by manasb on 12-11-2016.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        final ConfigService configService = new ConfigService();
        final ChatService chatService = ChatService.getInstance();

        port(configService.getPort());
        setupWebSocket(configService.getWebSocketEndpoint());
        enableCORS();
        setupApiEndpoints(configService, chatService);
    }

    private static void setupWebSocket(String endpoint) {
        webSocket("/" + endpoint, ChatWebSocket.class);
    }

    private static void setupApiEndpoints(final ConfigService configService, final ChatService chatService) {
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

    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS() {

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
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "GET,POST");
            response.header("Access-Control-Allow-Headers",
                    "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");

            response.type("application/json");
        });
    }
}
