namespace WpfClient.ViewModel
{
    public class ChatViewModel : ViewModelBase
    {

        public ConnectedUsersViewModel ConnectedUsersViewModel { get; }

        public ChatViewModel(ConnectedUsersViewModel connectedUsersViewModel)
        {
            ConnectedUsersViewModel = connectedUsersViewModel;
        }
    }
}