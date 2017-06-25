using System.Collections.ObjectModel;
using System.Windows.Input;
using Prism.Events;
using WpfClient.Command;
using WpfClient.Model;
using WpfClient.Util.Event;
using WpfCLient.DataAccess;

namespace WpfClient.ViewModel
{
    public class MessagesViewModel : ViewModelBase
    {
        public User CurrentUser { get; private set; }
        public ObservableCollection<MessagesListViewItem> Messages { get; }
        public bool IsNotLoading
        {
            get => isNotLoading;
            set
            {
                isNotLoading = value;
                OnPropertyChanged();
            }
        }
        public string MessageText
        {
            get => messageText;
            set
            {
                messageText = value;
                OnPropertyChanged();
            }
        }
        public ICommand SendMessageCommand { get; }

        private readonly IChatSocketApi chatSocketApi;
        private bool isNotLoading;
        private string messageText;

        public MessagesViewModel(IChatSocketApi chatSocketApi, IEventAggregator eventAggregator)
        {
            this.chatSocketApi = chatSocketApi;

            eventAggregator.GetEvent<UserAddedEvent>().Subscribe(OnUserAdded);
            eventAggregator.GetEvent<ChatMessageEvent>().Subscribe(OnChatMessage);

            Messages = new ObservableCollection<MessagesListViewItem>();
            SendMessageCommand = new DelegateCommand(SendMessage);
            IsNotLoading = true;
        }

        private async void SendMessage(object obj)
        {
            if (string.IsNullOrEmpty(MessageText))
            {
                return;
            };

            IsNotLoading = false;
            await chatSocketApi.SendChatMessage(CurrentUser, MessageText);
            IsNotLoading = true;
            MessageText = string.Empty;
        }

        public class MessagesListViewItem
        {
            public ChatMessage ChatMessage { get; }
            public bool IsSourceCurrentUser { get; }

            public MessagesListViewItem(ChatMessage chatMessage, bool isSourceCurrentUser)
            {
                ChatMessage = chatMessage;
                IsSourceCurrentUser = isSourceCurrentUser;
            }
        }

        private void OnUserAdded(User user)
        {
            Dispatcher.Invoke(() => CurrentUser = user);
        }

        private void OnChatMessage(ChatMessage chatMessage)
        {
            Dispatcher.Invoke(() => Messages.Add(new MessagesListViewItem(chatMessage, chatMessage.User.Equals(CurrentUser))));
        }
    }
}