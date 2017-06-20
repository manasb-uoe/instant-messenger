using System.Windows;
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
    }
}
