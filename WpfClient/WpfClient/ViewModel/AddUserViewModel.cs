using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using WpfClient.Command;

namespace WpfClient.ViewModel
{
    public class AddUserViewModel
    {

        public string Username { get; set; }
        public string ErrorMessage { get; private set; }
        public bool IsShowingErrorMessage { get; private set; }
        public ICommand AddUserCommand;

        public AddUserViewModel()
        {
            AddUserCommand = new DelegateCommand(OnGoButtonClicked);
        }

        private void OnGoButtonClicked(object o)
        {

        }
    }
}
