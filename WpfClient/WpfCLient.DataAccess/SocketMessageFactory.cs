using System.Collections.Generic;
using Newtonsoft.Json;

namespace WpfCLient.DataAccess
{
    public class SocketMessageFactory : ISocketMessageFactory
    {
        public string CreateConnectMessage(string username)
        {
            var json = new Dictionary<string, object>()
            {
                {"messageType",  SocketMessageType.Connect.Value},
                {"data", username }
            };

            return JsonConvert.SerializeObject(json);
        }
    }
}