import services.ChatService;
import services.ConfigService;
import server.Server;

/**
 * Created by manasb on 12-11-2016.
 */

public class Main {

    public static void main(String[] args) throws Exception {
        final ConfigService configService = ConfigService.getInstance();
        final ChatService chatService = ChatService.getInstance();

        new Server(configService, chatService).start();
    }
}
