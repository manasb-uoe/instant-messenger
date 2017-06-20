using System.Windows.Input;
using NLog;
using Prism.Events;
using WpfClient.Command;
using WpfClient.Event;
using WpfClient.Model;
using WpfClient.Util;
using WpfClient.View;
using WpfCLient.DataAccess;

namespace WpfClient.ViewModel
{
    public class AddUserViewModel : ViewModelBase
    {
        public string Username { get; set; }

        public string ErrorMessage
        {
            get => errorMessage;
            set
            {
                errorMessage = value;
                OnPropertyChanged();
            }
        }

        public bool IsShowingErrorMessage
        {
            get => isShowingErrorMessage;
            set
            {
                isShowingErrorMessage = value;
                OnPropertyChanged();
            }
        }

        public bool IsNotLoading
        {
            get => isNotLoading;
            set
            {
                isNotLoading = value;
                OnPropertyChanged();
            }
        }

        public ICommand AddUserCommand { get; }

        private bool isNotLoading;
        private string errorMessage;
        private bool isShowingErrorMessage;
        private readonly IUserApi userApi;
        private static readonly Logger Log = LogManager.GetCurrentClassLogger();
        private readonly WindowFactory<ChatWindow> chatWindowFactory;
        private readonly IEventAggregator eventAggregator;

        public AddUserViewModel(IUserApi userApi, WindowFactory<ChatWindow> chatWindowFactory, IEventAggregator eventAggregator)
        {
            this.userApi = userApi;
            this.chatWindowFactory = chatWindowFactory;
            this.eventAggregator = eventAggregator;

            AddUserCommand = new DelegateCommand(o => { AddUser(); });
            IsNotLoading = true;
        }

        private async void AddUser()
        {
            IsNotLoading = false;
            var apiResponse = await userApi.AddUserAsync(Username);
            IsNotLoading = true;

            if (apiResponse.IsSuccessful())
            {
                IsShowingErrorMessage = false;
                chatWindowFactory.ShowWindow();
                eventAggregator.GetEvent<CachedEvent<User>>().Publish(apiResponse.Body);
                Log.Info($"User with username [{Username}] added successfully.");
            }
            else
            {
                ErrorMessage = apiResponse.Error;
                IsShowingErrorMessage = true;
                Log.Error($"Failed to add user with username [{Username}]: [{apiResponse.Error}]");
            }
        }
    }
}