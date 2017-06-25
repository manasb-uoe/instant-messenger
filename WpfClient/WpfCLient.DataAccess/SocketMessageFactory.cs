using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using WpfClient.Model;

namespace WpfCLient.DataAccess
{
    public class SocketMessageFactory : ISocketMessageFactory
    {
        public string CreateConnectMessage(string username)
        {
            var json = new Dictionary<string, object>()
            {
                {"messageType", SocketMessageType.Connect.Value},
                {"data", username}
            };

            return JsonConvert.SerializeObject(json);
        }

        public string CreateChatMessage(User user, string messageText)
        {
            var json = new Dictionary<string, object>()
            {
                {"messageType", SocketMessageType.ChatMessage.Value},
                {
                    "data", new Dictionary<string, object>()
                    {
                        {"source", MessageSource.User.Value},
                        {"user", new Dictionary<string, object>() {{"username", user.Username}}},
                        {"messageText", messageText},
                        {"timestamp", DateTimeOffset.Now.ToUnixTimeMilliseconds()}
                    }
                }
            };

            return JsonConvert.SerializeObject(json);
        }
    }
}