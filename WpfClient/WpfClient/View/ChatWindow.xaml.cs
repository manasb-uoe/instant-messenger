using System.ComponentModel;
using System.Windows;
using WpfClient.ViewModel;

namespace WpfClient.View
{
    /// <summary>
    /// Interaction logic for ChatWindow.xaml
    /// </summary>
    public partial class ChatWindow : Window
    {
        public ChatViewModel ChatViewModel { get; }

        public ChatWindow(ChatViewModel chatViewModel)
        {
            ChatViewModel = chatViewModel;
            DataContext = ChatViewModel;
            InitializeComponent();
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            ChatViewModel.Dispose();
            base.OnClosing(e);
        }
    }
}