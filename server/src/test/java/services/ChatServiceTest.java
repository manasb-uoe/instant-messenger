package services;

import domain.*;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static java.lang.Math.abs;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by manasb on 23-12-2016.
 */

public class ChatServiceTest {

    private final ChatService unitUnderTest = ChatService.getInstance();
    private final Session mockSessionOne = mock(Session.class);
    private final Session mockSessionTwo = mock(Session.class);
    private final RemoteEndpoint mockRemoteOne = mock(RemoteEndpoint.class);
    private final RemoteEndpoint mockRemoteTwo = mock(RemoteEndpoint.class);
    private static final String USERNAME_USER_1 = "User 1";
    private static final String USERNAME_USER_2 = "User 2";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void beforeTest() {
        unitUnderTest.sessionUserMap.clear();
        unitUnderTest.connectedUsers.clear();

        when(mockSessionOne.getRemote()).thenReturn(mockRemoteOne);
        when(mockSessionTwo.getRemote()).thenReturn(mockRemoteTwo);
    }

    @Test
    public void shouldBeSingleton() {
        assertThat(unitUnderTest, is(ChatService.getInstance()));
    }

    @Test
    public void shouldAddNewUser() throws Exception {
        final String twentyCharsUsername = "11111111111111111111";
        final User actualUser = unitUnderTest.addUser(twentyCharsUsername);

        final User expectedUser = new User(twentyCharsUsername);
        assertThat(actualUser, is(expectedUser));
        assertThat(unitUnderTest.connectedUsers.size(), is(1));
        assertThat(unitUnderTest.connectedUsers.get(0), is(expectedUser));
        assertThat(unitUnderTest.sessionUserMap.size(), is(0));
    }

    @Test
    public void shouldThrowExceptionWhenAddingUserWithBlankUsername() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Username must be at least 1 character long.");

        unitUnderTest.addUser("");
    }

    @Test
    public void shouldThrowExceptionWhenAddingUserWithLongUsername() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Username must be at most 20 characters long.");

        String twentyOneCharsUsername = "111111111111111111111";
        unitUnderTest.addUser(twentyOneCharsUsername);
    }

    @Test
    public void shouldThrowExceptionWhenAddingUserWithExistingUsername() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Another user with the same username already exists.");

        unitUnderTest.addUser(USERNAME_USER_1);
        unitUnderTest.addUser(USERNAME_USER_1);
    }

    @Test
    public void shouldBindSessionToUser() throws Exception {
        unitUnderTest.addUser(USERNAME_USER_1);
        final User actualUser = unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);

        final User expectedUser = new User(USERNAME_USER_1);
        assertThat(actualUser, is(expectedUser));
        assertThat(unitUnderTest.sessionUserMap.size(), is(1));
        assertThat(unitUnderTest.sessionUserMap.get(mockSessionOne), is(expectedUser));
    }

    @Test
    public void shouldNotBindSessionToUserIfBindingAlreadyExists() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage(String.format("User with username [%s] is already bound to a session.",
                USERNAME_USER_1));

        unitUnderTest.addUser(USERNAME_USER_1);
        unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
        unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
    }

    @Test
    public void shouldNotBindSessionToUserIfNoUserExists() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage(String.format("No user with username [%s] exists.", USERNAME_USER_1));

        unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        unitUnderTest.addUser(USERNAME_USER_1);
        unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
        final User userToRemain = unitUnderTest.addUser(USERNAME_USER_2);
        unitUnderTest.bindSessionToUser(mockSessionTwo, USERNAME_USER_2);
        unitUnderTest.removeUser(mockSessionOne);

        assertThat(unitUnderTest.sessionUserMap.size(), is(1));
        assertThat(unitUnderTest.sessionUserMap.containsKey(mockSessionOne), is(false));
        assertThat(unitUnderTest.sessionUserMap.get(mockSessionTwo), is(userToRemain));
    }

    @Test
    public void shouldThrowExceptionWhenRemovingUserNotBoundToASession() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Failed to remove user since session does not exist.");

        unitUnderTest.addUser(USERNAME_USER_1);
        unitUnderTest.removeUser(mockSessionOne);
    }

    @Test
    public void shouldBroadcastConnectedUsers() throws Exception {
        unitUnderTest.addUser(USERNAME_USER_1);
        unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
        unitUnderTest.addUser(USERNAME_USER_2);
        unitUnderTest.bindSessionToUser(mockSessionTwo, USERNAME_USER_2);
        unitUnderTest.broadcastConnectedUsers();

        final SocketMessage actualSocketMessageOne = captureSocketMessage(mockRemoteOne);
        assertThat(actualSocketMessageOne.getMessageType(), is(MessageType.CONNECTED_USERS));
        assertThat(actualSocketMessageOne.getData(), is(unitUnderTest.connectedUsers));

        final SocketMessage actualSocketMessageTwo = captureSocketMessage(mockRemoteTwo);
        assertThat(actualSocketMessageTwo.getMessageType(), is(MessageType.CONNECTED_USERS));
        assertThat(actualSocketMessageTwo.getData(), is(unitUnderTest.connectedUsers));
    }

    private SocketMessage captureSocketMessage(final RemoteEndpoint remote) throws IOException {
        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(remote).sendString(argumentCaptor.capture());
        return SocketMessage.fromJson(argumentCaptor.getValue());
    }

    @Test
    public void shouldSendErrorToUser() throws IOException {
        unitUnderTest.sendErrorToUser("Error", mockSessionOne);

        final SocketMessage actualSocketMessage = captureSocketMessage(mockRemoteOne);
        assertThat(actualSocketMessage.getMessageType(), is(MessageType.ERROR));
        assertThat(actualSocketMessage.getData(), is("Error"));
    }

    @Test
    public void shouldBroadcastUserConnectedSystemMessage() throws Exception {
        unitUnderTest.addUser(USERNAME_USER_1);
        final User user = unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
        unitUnderTest.broadcastUserConnectedSystemMessage(user);

        final ChatMessage expectedChatMessage = new ChatMessage(
                MessageSource.SYSTEM,
                new User("System"),
                user.getUsername() + " has joined the chat.",
                System.currentTimeMillis()
        );
        final SocketMessage actualSocketMessage = captureSocketMessage(mockRemoteOne);
        final ChatMessage actualChatMessage = (ChatMessage) actualSocketMessage.getData();

        assertThat(actualSocketMessage.getMessageType(), is(MessageType.CHAT_MESSAGE));
        assertThat(actualChatMessage.getMessageText(), is(expectedChatMessage.getMessageText()));
        assertThat(actualChatMessage.getSource(), is(expectedChatMessage.getSource()));
        assertThat(actualChatMessage.getUser(), is(expectedChatMessage.getUser()));
        assertTrue(abs(actualChatMessage.getTimestamp() - expectedChatMessage.getTimestamp()) <= 1000);
    }

    @Test
    public void shouldBroadcastUserDisconnectedSystemMessage() throws Exception {
        unitUnderTest.addUser(USERNAME_USER_1);
        final User removedUser = unitUnderTest.bindSessionToUser(mockSessionOne, USERNAME_USER_1);
        unitUnderTest.broadcastUserDisconnectedSystemMessage(removedUser);

        final ChatMessage expectedChatMessage = new ChatMessage(
                MessageSource.SYSTEM,
                new User("System"),
                removedUser.getUsername() + " has left the chat.",
                System.currentTimeMillis()
        );
        final SocketMessage actualSocketMessage = captureSocketMessage(mockRemoteOne);
        final ChatMessage actualChatMessage = (ChatMessage) actualSocketMessage.getData();

        assertThat(actualSocketMessage.getMessageType(), is(MessageType.CHAT_MESSAGE));
        assertThat(actualChatMessage.getMessageText(), is(expectedChatMessage.getMessageText()));
        assertThat(actualChatMessage.getSource(), is(expectedChatMessage.getSource()));
        assertThat(actualChatMessage.getUser(), is(expectedChatMessage.getUser()));
        assertTrue(abs(actualChatMessage.getTimestamp() - expectedChatMessage.getTimestamp()) <= 1000);
    }
}
