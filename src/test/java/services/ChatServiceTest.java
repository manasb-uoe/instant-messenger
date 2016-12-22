package services;

import models.User;
import models.socketmessages.ConnectedUsersSocketMessage;
import models.socketmessages.ErrorSocketMessage;
import models.socketmessages.IdentitySocketMessage;
import models.socketmessages.SocketMessage;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
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
        unitUnderTest.removeUser(mockSessionOne);

        assertThat(unitUnderTest.sessionUserMap.size(), is(1));
        assertFalse(unitUnderTest.sessionUserMap.containsKey(mockSessionOne));
    }

    @Test
    public void shouldBroadcastConnectedUsers() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.addUser(mockSessionTwo);
        unitUnderTest.broadcastConnectedUsers();

        final List<User> connectedUsers = Arrays.asList(new User("User 1"), new User("User 2"));
        final SocketMessage connectedUsersSocketMessage = new ConnectedUsersSocketMessage(connectedUsers);
        verify(mockRemoteOne).sendString(connectedUsersSocketMessage.toJson());
        verify(mockRemoteTwo).sendString(connectedUsersSocketMessage.toJson());
    }

    @Test
    public void shouldSendErrorToUser() throws IOException {
        unitUnderTest.sendErrorToUser("Error", mockSessionOne);

        final SocketMessage errorSocketMessage = new ErrorSocketMessage("Error");
        verify(mockRemoteOne).sendString(errorSocketMessage.toJson());
    }

    @Test
    public void shouldSendIdentityToSession() throws Exception {
        unitUnderTest.addUser(mockSessionOne);
        unitUnderTest.sendIdentityToSession(mockSessionOne);

        final SocketMessage identitySocketMessage = new IdentitySocketMessage(new User("User 1"));
        verify(mockRemoteOne).sendString(identitySocketMessage.toJson());
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
