using System;
using System.Windows.Input;

namespace WpfClient.Command
{
    public class DelegateCommand : ICommand {

        private readonly Action<object> execute;
        private readonly Func<object, bool> canExecute;

        public event EventHandler CanExecuteChanged;

        public DelegateCommand(Action<object> execute, Func<object, bool> canExecute = null) {
            if (execute == null) {
                throw new ArgumentNullException(nameof(execute));
            }

            this.execute = execute;
            this.canExecute = canExecute;
        }

        public bool CanExecute(object parameter) {
            return canExecute == null || canExecute(parameter);
        }

        public void Execute(object parameter) {
            execute(parameter);
        }

        public void RaiseCanExecuteChangedEvent() {
            CanExecuteChanged.Invoke(this, EventArgs.Empty);
        }
    }
}