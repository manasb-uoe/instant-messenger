using System;

namespace WpfClient.Model
{
    public class MessageSource
    {
        public string Value { get; }

        private MessageSource(string value)
        {
            Value = value;
        }

        public static readonly MessageSource User = new MessageSource("USER");
        public static readonly MessageSource System = new MessageSource("SYSTEM");

        public static MessageSource FromString(string source)
        {
            if (source.Equals(User.Value))
            {
                return User;
            }
            else if (source.Equals(System.Value))
            {
                return System;
            }
            else
            {
                throw new ArgumentException($"Invalid source [{source}]. Allowed valyes are [{User.Value},{System.Value}]");
            }
        }
    }
}