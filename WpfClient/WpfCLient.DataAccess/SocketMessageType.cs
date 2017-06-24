namespace WpfCLient.DataAccess
{
    public class SocketMessageType
    {
        public string Value { get; }

        private SocketMessageType(string value)
        {
            Value = value;
        }

        public static readonly SocketMessageType Connect = new SocketMessageType("CONNECT");
        public static readonly SocketMessageType ChatMessage = new SocketMessageType("CHAT_MESSAGE");
        public static readonly SocketMessageType ConnectedUsers = new SocketMessageType("CONNECTED_USERS");
    }
}