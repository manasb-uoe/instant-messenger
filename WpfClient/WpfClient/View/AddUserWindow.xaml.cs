using System.Windows;
using System.Windows.Navigation;
using WpfClient.ViewModel;

namespace WpfClient.View
{
    /// <summary>
    /// Interaction logic for AddUserWindow.xaml
    /// </summary>
    public partial class AddUserWindow : Window
    {
        private AddUserViewModel addUserViewModel;

        public AddUserWindow(AddUserViewModel addUserViewModel)
        {
            InitializeComponent();

            this.addUserViewModel = addUserViewModel;
            DataContext = addUserViewModel;
        }

        private void Hyperlink_OnRequestNavigate(object sender, RequestNavigateEventArgs e)
        {
            System.Diagnostics.Process.Start(e.Uri.ToString());
        }
    }
}
