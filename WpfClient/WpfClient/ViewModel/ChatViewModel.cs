using System;
using Prism.Events;
using WpfClient.Model;
using WpfClient.Util.Event;
using WpfCLient.DataAccess;

namespace WpfClient.ViewModel
{
    public class ChatViewModel : ViewModelBase, IDisposable
    {
        public ConnectedUsersViewModel ConnectedUsersViewModel { get; }
        public MessagesViewModel MessagesViewModel { get; }
        private readonly IChatSocketApi chatSocketApi;

        public ChatViewModel(ConnectedUsersViewModel connectedUsersViewModel, MessagesViewModel messagesViewModel,
            IChatSocketApi chatSocketApi, IEventAggregator eventAggregator)
        {
            ConnectedUsersViewModel = connectedUsersViewModel;
            MessagesViewModel = messagesViewModel;
            this.chatSocketApi = chatSocketApi;

            chatSocketApi.OpenSocketConnection();
            eventAggregator.GetEvent<UserAddedEvent>().Subscribe(OnUserAdded);
        }

        public void Dispose()
        {
            chatSocketApi?.Dispose();
        }

        private void OnUserAdded(User user)
        {
            chatSocketApi.SendConnectMessage(user.Username);
        }
    }
}