using System.Windows;
using Autofac;

namespace WpfClient.Util
{
    public class WindowFactory<T> where T : Window
    {
        private readonly IComponentContext container;

        public WindowFactory(IComponentContext container)
        {
            this.container = container;
        }

        public void ShowWindow()
        {
            this.container.Resolve<T>().Show();
        }
    }
}