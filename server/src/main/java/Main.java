import domain.User;
import services.ChatService;
import services.ConfigService;
import utils.HttpResponseFactory;
import websocket.ChatWebSocket;

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
        setupApiEndpoints(chatService);
    }

    private static void setupWebSocket(String endpoint) {
        webSocket("/" + endpoint, ChatWebSocket.class);
    }

    private static void setupApiEndpoints(final ChatService chatService) {
        post("/api/add-user", (request, response) -> {
            try {
                User user = chatService.addUser(request.body());
                return HttpResponseFactory.createOkResponse(response, user);
            } catch (Exception e) {
                return HttpResponseFactory.createBadRequestResponse(response, e.getMessage());
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
