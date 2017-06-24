using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Threading;
using WpfClient.Annotations;

namespace WpfClient.ViewModel
{
    public class ViewModelBase : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public Dispatcher Dispatcher { get; }

        public ViewModelBase()
        {
            Dispatcher = Dispatcher.CurrentDispatcher;;
        }

        [NotifyPropertyChangedInvocator]
        protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}