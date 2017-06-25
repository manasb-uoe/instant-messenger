using System.Collections.Specialized;
using System.Windows.Controls;

namespace WpfClient.View
{
    /// <summary>
    /// Interaction logic for Messages.xaml
    /// </summary>
    public partial class Messages : UserControl
    {
        public Messages()
        {
            InitializeComponent();

            // Automatically scroll to bottom of listview whenever a new item is added
            (MessagesListView.Items as INotifyCollectionChanged).CollectionChanged +=
                (sender, args) =>
                {
                    if (MessagesListView.Items.Count > 0)
                    {
                        MessagesListView.ScrollIntoView(MessagesListView.Items[MessagesListView.Items.Count - 1]);
                    }
                };
        }
    }
}
