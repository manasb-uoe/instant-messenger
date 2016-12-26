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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void beforeTest() {
        unitUnderTest.sessionUserMap.clear();
        unitUnderTest.currentUserNumber.set(1);

        when(mockSessionOne.getRemote()).thenReturn(mockRemoteOne);
        when(mockSessionTwo.getRemote()).thenReturn(mockRemoteTwo);
    }

    @Test
    public void shouldBeSingleton() {
        assertThat(unitUnderTest, is(ChatService.getInstance()));
    }

    @Test
    public void shouldAddNewUser() throws Exception {
        unitUnderTest.addUser(mockSessionOne);

        assertThat(unitUnderTest.currentUserNumber.get(), is(2));
        assertThat(unitUnderTest.sessionUserMap.size(), is(1));
        assertThat(unitUnderTest.sessionUserMap.get(mockSessionOne), is(new User("User 1")));
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.addUser(mockSessionTwo);

        final Optional<User> removedUserOptional = unitUnderTest.removeUser(mockSessionOne);

        if (!removedUserOptional.isPresent()) {
            fail("User should not be null");
            return;
        }

        assertThat(removedUserOptional.get().getUsername(), is("User 1"));
        assertThat(unitUnderTest.sessionUserMap.size(), is(1));
        assertFalse(unitUnderTest.sessionUserMap.containsKey(mockSessionOne));
    }

    @Test
    public void shouldBroadcastConnectedUsers() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.addUser(mockSessionTwo);
        unitUnderTest.broadcastConnectedUsers();

        final List<User> connectedUsers = Arrays.asList(new User("User 1"), new User("User 2"));
        final SocketMessage<List<User>> connectedUsersSocketMessage =
                SocketMessageFactory.createConnectedUsersMessage(connectedUsers);
        verify(mockRemoteOne).sendString(connectedUsersSocketMessage.toJson());
        verify(mockRemoteTwo).sendString(connectedUsersSocketMessage.toJson());
    }

    @Test
    public void shouldSendErrorToUser() throws IOException {
        unitUnderTest.sendErrorToUser("Error", mockSessionOne);

        final SocketMessage<String> errorSocketMessage = SocketMessageFactory.createErrorMessage("Error");
        verify(mockRemoteOne).sendString(errorSocketMessage.toJson());
    }

    @Test
    public void shouldSendIdentityToSession() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.sendIdentityToSession(mockSessionOne);

        final SocketMessage<User> identitySocketMessage =
                SocketMessageFactory.createIdentityMessage(new User("User 1"));
        verify(mockRemoteOne).sendString(identitySocketMessage.toJson());
    }

    @Test
    public void shouldBroadcastUserConnectedSystemMessage() throws Exception {
        final User user = unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.broadcastUserConnectedSystemMessage(user);

        final ChatMessage expectedChatMessage = new ChatMessage(
                MessageSource.SYSTEM,
                new User("System"),
                user.getUsername() + " has joined the chat.",
                System.currentTimeMillis()
        );

        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRemoteOne).sendString(argumentCaptor.capture());
        final String actual = argumentCaptor.getValue();
        final SocketMessage actualChatSocketMessage = SocketMessage.fromJson(actual);
        final ChatMessage actualChatMessage = (ChatMessage) actualChatSocketMessage.getData();

        assertThat(actualChatSocketMessage.getMessageType(), is(MessageType.CHAT_MESSAGE));
        assertThat(actualChatMessage.getMessageText(), is(expectedChatMessage.getMessageText()));
        assertThat(actualChatMessage.getSource(), is(expectedChatMessage.getSource()));
        assertThat(actualChatMessage.getUser(), is(expectedChatMessage.getUser()));
        assertTrue(abs(actualChatMessage.getTimestamp() - expectedChatMessage.getTimestamp()) <= 1000);
    }

    @Test
    public void shouldBroadcastUserDisconnectedSystemMessage() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.addUser(mockSessionTwo);

        Optional<User> removedUserOptional = unitUnderTest.removeUser(mockSessionOne);

        if (!removedUserOptional.isPresent()) {
            fail("User should not be null");
            return;
        }

        final User removedUser = removedUserOptional.get();
        unitUnderTest.broadcastUserDisconnectedSystemMessage(removedUser);

        final ChatMessage expectedChatMessage = new ChatMessage(
                MessageSource.SYSTEM,
                new User("System"),
                removedUser.getUsername() + " has left the chat.",
                System.currentTimeMillis()
        );

        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRemoteTwo).sendString(argumentCaptor.capture());
        final String actual = argumentCaptor.getValue();
        final SocketMessage actualChatSocketMessage = SocketMessage.fromJson(actual);
        final ChatMessage actualChatMessage = (ChatMessage) actualChatSocketMessage.getData();

        assertThat(actualChatSocketMessage.getMessageType(), is(MessageType.CHAT_MESSAGE));
        assertThat(actualChatMessage.getMessageText(), is(expectedChatMessage.getMessageText()));
        assertThat(actualChatMessage.getSource(), is(expectedChatMessage.getSource()));
        assertThat(actualChatMessage.getUser(), is(expectedChatMessage.getUser()));
        assertTrue(abs(actualChatMessage.getTimestamp() - expectedChatMessage.getTimestamp()) <= 1000);
    }

    @Test
    public void shouldUpdateUsername() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.updateUsername("User 1", "New");

        assertThat(unitUnderTest.sessionUserMap.get(mockSessionOne).getUsername(), is("New"));
    }

    @Test
    public void shouldThrowExceptionIfTryingToUpdateUsernameOfNonExistingUser() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("No user with username [User 1] exists");

        unitUnderTest.updateUsername("User 1", "New");
    }

    @Test
    public void shouldThrowExceptionIfNewUsernameAlreadyExists() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.addUser(mockSessionTwo);

        expectedException.expect(Exception.class);
        expectedException.expectMessage("Another user with the same username already exists.");

        unitUnderTest.updateUsername("User 1", "User 2");
    }

}
