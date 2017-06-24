using System;
using System.IO;
using System.Reflection;
using Autofac;
using Prism.Events;
using WpfClient.Util;
using WpfClient.Util.Config;
using WpfClient.View;
using WpfClient.ViewModel;
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
            builder.RegisterType<ChatSocketApi>().As<IChatSocketApi>();
            builder.RegisterType<SocketMessageFactory>().As<ISocketMessageFactory>();

            var config = LoadAppConfig();
            builder.RegisterInstance(config).AsSelf();

            return builder.Build();
        }

        private static Config LoadAppConfig()
        {
            var commandLineArgParser = new CommandLineArgParser(Environment.GetCommandLineArgs());
            var executionAssemblyDir = Assembly.GetExecutingAssembly().Location;
            var configPath = Path.Combine(Path.GetDirectoryName(executionAssemblyDir), $"Config\\{commandLineArgParser.Environment}\\config.json");
            var config = new ConfigLoader(configPath).Load();
            return config;
        }
    }
}