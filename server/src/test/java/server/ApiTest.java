package server;

import domain.Environment;
import domain.User;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import services.ChatService;
import services.ConfigService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * Created by manasb on 29-12-2016.
 */
public class ApiTest {

    private static Server server;
    private static final ConfigService mockConfigService = mock(ConfigService.class);
    private static final ChatService mockChatService = mock(ChatService.class);
    private static final String USERNAME_USER_1_SUCCESS = "User 1";
    private static final String USERNAME_USER_2_ERROR = "User 2";
    private static final String ADD_USER_ENDPOINT = "/api/add-user";

    @BeforeClass
    public static void beforeClass() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4567;

        when(mockConfigService.getPort()).thenReturn(4567);
        when(mockConfigService.getEnvironment()).thenReturn(Environment.DEV);

        when(mockChatService.addUser(USERNAME_USER_1_SUCCESS)).thenReturn(new User(USERNAME_USER_1_SUCCESS));
        when(mockChatService.addUser(USERNAME_USER_2_ERROR)).thenThrow(new Exception("Error"));

        server = new Server(mockConfigService, mockChatService);
        server.start();
    }

    @AfterClass
    public static void afterClass() {
        server.stop();
    }

    @Test
    public void addUserEndpointShouldReturnSuccessResponseCorrectly() throws Exception {
        given().body(USERNAME_USER_1_SUCCESS)
                .when().post(ADD_USER_ENDPOINT)
                .then().statusCode(200).body("statusCode", equalTo(200)).body("data.username",
                equalTo(USERNAME_USER_1_SUCCESS));

        verify(mockChatService).addUser(USERNAME_USER_1_SUCCESS);
    }

    @Test
    public void addUserEndpointShouldReturnErrorResponseCorrectly() throws Exception {
        given().body(USERNAME_USER_2_ERROR)
                .when().post(ADD_USER_ENDPOINT)
                .then().statusCode(400).body("statusCode", equalTo(400)).body("error",
                equalTo("Error"));

        verify(mockChatService).addUser(USERNAME_USER_2_ERROR);
    }

}
