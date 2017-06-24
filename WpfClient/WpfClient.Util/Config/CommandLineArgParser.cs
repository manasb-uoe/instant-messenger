using System;
using System.Linq;

namespace WpfClient.Util.Config
{
    public class CommandLineArgParser
    {
        public string Environment { get; }
        private static readonly string[] Environments = { "dev", "prod" };

        public CommandLineArgParser(string[] commandLineArgs)
        {
            if (commandLineArgs.Length == 1)
            {
                throw new ArgumentException("No command line arguments were provided.");
            }

            if (!Environments.Contains(commandLineArgs[1]))
            {
                throw new ArgumentException(
                    $"[{commandLineArgs[1]}] is not a valid environment. Allowed values for " +
                    $"environment are: {Environments.ToString()}");
            }

            Environment = commandLineArgs[1];
        }
    }
}