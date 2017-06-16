namespace WpfClient.ViewModel
{
    public class MainViewModel
    {
        public ConnectedUsersViewModel ConnectedUsersViewModel { get; private set; }

        public MainViewModel(ConnectedUsersViewModel connectedUsersViewModel)
        {
            ConnectedUsersViewModel = connectedUsersViewModel;
        }
    }
}