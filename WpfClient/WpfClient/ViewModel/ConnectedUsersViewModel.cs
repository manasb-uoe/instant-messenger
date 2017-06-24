using System.Collections.Generic;
using System.Collections.ObjectModel;
using NLog;
using Prism.Events;
using WpfClient.Model;
using WpfClient.Util.Event;

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

        public ObservableCollection<ConnectedUsersListViewItem> ConnectedUsers { get; }

        private User currentUser;

        private static readonly ILogger Logger = LogManager.GetCurrentClassLogger();

        public ConnectedUsersViewModel(IEventAggregator eventAggregator)
        {
            ConnectedUsers = new ObservableCollection<ConnectedUsersListViewItem>();

            eventAggregator.GetEvent<UserAddedEvent>().Subscribe(OnUserAdded);
            eventAggregator.GetEvent<ConnectedUsersEvent>().Subscribe(OnUsersConnected);
        }

        public class ConnectedUsersListViewItem
        {
            public User User { get; }
            public bool IsCurrentUser { get; }

            public ConnectedUsersListViewItem(User user, bool isCurrentUser)
            {
                User = user;
                IsCurrentUser = isCurrentUser;
            }
        }

        private void OnUserAdded(User user)
        {
            Logger.Info("User received: " + user);

            Dispatcher.Invoke(() => { CurrentUser = user; });
        }

        private void OnUsersConnected(List<User> users)
        {
            Dispatcher.Invoke(() =>
            {
                ConnectedUsers.Clear();
                users.ForEach(user => ConnectedUsers.Add(
                    new ConnectedUsersListViewItem(user, user.Username.Equals(CurrentUser.Username))));
            });
        }
    }
}