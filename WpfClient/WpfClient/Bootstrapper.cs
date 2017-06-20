using System.IO;
using Autofac;
using Prism.Events;
using WpfClient.Util;
using WpfClient.View;
using WpfClient.ViewModel;
using WpfClient.Util.Config;
using WpfCLient.DataAccess;

namespace WpfClient
{
    public class Bootstrapper
    {
        public IContainer Bootstrap()
        {
            var builder = new ContainerBuilder();

            builder.RegisterType<ConnectedUsersViewModel>().AsSelf();
            builder.RegisterType<AddUserWindow>().AsSelf();
            builder.RegisterType<ChatViewModel>().AsSelf();
            builder.RegisterType<ChatWindow>().AsSelf();
            builder.RegisterType<AddUserViewModel>().AsSelf();
            builder.RegisterType<UserApi>().As<IUserApi>();
            builder.RegisterType<HttpHandler>().As<IHttpHandler>();
            builder.RegisterType<WindowFactory<ChatWindow>>().AsSelf();
            builder.RegisterType<EventAggregator>().As<IEventAggregator>().SingleInstance();

            var config = LoadAppConfig();
            builder.RegisterInstance(config).AsSelf();

            return builder.Build();
        }

        private static Config LoadAppConfig()
        {
            var executionAssemblyDir = System.Reflection.Assembly.GetExecutingAssembly().Location;
            var configPath = Path.Combine(System.IO.Path.GetDirectoryName(executionAssemblyDir), "Config\\config.json");
            var config = new ConfigLoader(configPath).Load();
            return config;
        }
    }
}