using System.Collections.ObjectModel;
using NLog;
using Prism.Events;
using WpfClient.Event;
using WpfClient.Model;

namespace WpfClient.ViewModel
{
    public class ConnectedUsersViewModel : ViewModelBase
    {
        public User CurrentUser
        {
            get => currentUser;
            set
            {
                currentUser = value;
                OnPropertyChanged();
            }
        }

        public string Heading { get; }
        public ObservableCollection<string> ConnectedUsers { get; }

        private User currentUser;
        private IEventAggregator eventAggregator;
        private static readonly ILogger Logger = LogManager.GetCurrentClassLogger();

        public ConnectedUsersViewModel(IEventAggregator eventAggregator)
        {
            this.eventAggregator = eventAggregator;
            eventAggregator.GetEvent<CachedEvent<User>>().Subscribe(OnUserAdded);
            Heading = "Connected Users";
            ConnectedUsers = new ObservableCollection<string>()
            {
                "User One",
                "User Two",
                "User Three",
                "User Four"
            };
        }

        private void OnUserAdded(User user)
        {
            Logger.Info("User received: " + user);
            CurrentUser = user;
        }
    }
}