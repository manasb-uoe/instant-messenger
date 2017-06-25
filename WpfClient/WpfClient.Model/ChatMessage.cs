using Newtonsoft.Json;

namespace WpfClient.Model
{
    public class ChatMessage
    {
        public MessageSource Source { get; set; }
        public User User { get; set; }
        public string MessageText { get; set; }
        public long Timestamp { get; set; }

        public ChatMessage(MessageSource source, User user, string messageText, long timestamp)
        {
            Source = source;
            User = user;
            MessageText = messageText;
            Timestamp = timestamp;
        }
    }
}