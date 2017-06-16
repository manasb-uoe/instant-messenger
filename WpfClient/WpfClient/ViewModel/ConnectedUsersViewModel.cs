using System.Collections.ObjectModel;

namespace WpfClient.ViewModel
{
    public class ConnectedUsersViewModel
    {
        public string Heading { get; private set; }
        public ObservableCollection<string> ConnectedUsers { get; private set; }

        public ConnectedUsersViewModel()
        {
            Heading = "Connected Users";
            ConnectedUsers = new ObservableCollection<string>() {
                "User One",
                "User Two",
                "User Three",
                "User Four"
            };
        }
    }
}