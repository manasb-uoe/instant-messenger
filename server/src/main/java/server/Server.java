package server;

import domain.User;
import services.ChatService;
import services.ConfigService;
import spark.Spark;
import utils.HttpResponseFactory;

import static spark.Spark.*;

/**
 * Created by manasb on 29-12-2016.
 */
public class Server {

    private final ConfigService configService;
    private final ChatService chatService;

    public Server(final ConfigService configService, final ChatService chatService) {
        this.configService = configService;
        this.chatService = chatService;
    }

    public void start() {
        port(configService.getPort());
        setupWebSocket(configService.getWebSocketEndpoint());
        enableCORS();
        setupApiEndpoints(chatService);
    }

    public void stop() {
        Spark.stop();
    }

    private void setupWebSocket(String endpoint) {
        webSocket("/" + endpoint, ChatWebSocket.class);
    }

    private void setupApiEndpoints(final ChatService chatService) {
        post("/api/add-user", (request, response) -> {
            try {
                User user = chatService.addUser(request.body());
                return HttpResponseFactory.createOkResponse(response, user);
            } catch (Exception e) {
                return HttpResponseFactory.createBadRequestResponse(response, e.getMessage());
            }
        });
    }

    private void enableCORS() {

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
