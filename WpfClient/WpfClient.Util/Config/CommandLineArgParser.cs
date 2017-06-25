using System;
using System.Linq;
using NLog;
using Prism.Logging;

namespace WpfClient.Util.Config
{
    public class CommandLineArgParser
    {
        public string Environment { get; }
        private static readonly string[] Environments = { "dev", "prod" };
        private static readonly ILogger Logger = LogManager.GetCurrentClassLogger();

        public CommandLineArgParser(string[] commandLineArgs)
        {
            if (commandLineArgs.Length == 1)
            {
                Logger.Error("No command line arguments were provided. Enivronment will be set to 'prod'.");
                Environment = "prod";
                return;
            }

            if (!Environments.Contains(commandLineArgs[1]))
            {
                var message = $"[{commandLineArgs[1]}] is not a valid environment. Allowed values for " +
                              $"environment are: {Environments.ToString()}";
                Logger.Error(message);
                throw new ArgumentException(message);
            }

            Environment = commandLineArgs[1];
        }
    }
}