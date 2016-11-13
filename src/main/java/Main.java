import static spark.Spark.*;

/**
 * Created by manasb on 12-11-2016.
 */

public class Main {

    public static void main(String[] args) {
        webSocket("/chat", ChatWebSocket.class);
        init();
    }

}
