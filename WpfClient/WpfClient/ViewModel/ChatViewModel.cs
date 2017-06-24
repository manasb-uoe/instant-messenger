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
        private readonly IChatSocketApi chatSocketApi;
        private readonly IEventAggregator eventAggregator;

        public ChatViewModel(ConnectedUsersViewModel connectedUsersViewModel, IChatSocketApi chatSocketApi, IEventAggregator eventAggregator)
        {
            ConnectedUsersViewModel = connectedUsersViewModel;
            this.chatSocketApi = chatSocketApi;
            this.eventAggregator = eventAggregator;

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