using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
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
