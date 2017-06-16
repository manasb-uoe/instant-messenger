using System.IO;
using Autofac;
using WpfClient.Util;
using WpfClient.View;
using WpfClient.ViewModel;

namespace WpfClient
{
    public class Bootstrapper
    {
        public IContainer Bootstrap()
        {
            var builder = new ContainerBuilder();

            builder.RegisterType<MainWindow>().AsSelf();
            builder.RegisterType<MainViewModel>().AsSelf();
            builder.RegisterType<ConnectedUsersViewModel>().AsSelf();
            builder.RegisterType<AddUserWindow>().AsSelf();
            builder.RegisterType<AddUserViewModel>().AsSelf();

            var executionAssemblyDir = System.Reflection.Assembly.GetExecutingAssembly().Location;
            var configPath = Path.Combine(System.IO.Path.GetDirectoryName(executionAssemblyDir), "Config\\config.json");
            var config = new ConfigLoader(configPath).Load();
            builder.RegisterInstance(config).AsSelf();

            return builder.Build();
        }
    }
}