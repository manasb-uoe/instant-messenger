using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Threading;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using NLog;
using Prism.Events;
using WebSocketSharp;
using WpfClient.Model;
using WpfClient.Util.Config;
using WpfClient.Util.Event;

namespace WpfCLient.DataAccess
{
    public class ChatSocketApi : IChatSocketApi
    {

        private static readonly ILogger Logger = LogManager.GetCurrentClassLogger();
        private readonly string webSocketEndPoint;
        private WebSocket ws;
        private readonly ISocketMessageFactory socketMessageFactory;
        private readonly IEventAggregator eventAggregator;

        public ChatSocketApi(Config config, ISocketMessageFactory socketMessageFactory, IEventAggregator eventAggregator)
        {
            webSocketEndPoint = config.ChatWebSocketEndpoint;
            this.socketMessageFactory = socketMessageFactory;
            this.eventAggregator = eventAggregator;
        }

        public async void OpenSocketConnection()
        {
            ws = new WebSocket(webSocketEndPoint, CancellationToken.None, 102392, OnOpen, OnClose, OnMessage, OnError);
            var connected = await ws.Connect().ConfigureAwait(false);
            if (connected)
            {
                Logger.Info($"WebSocket connection to {webSocketEndPoint} has been established");
            }
            else
            {
                Logger.Error($"Failed to establish web socket connection to [{webSocketEndPoint}]");
            }
        }

        public Task<bool> SendConnectMessage(string username)
        {
            var connectMessage = socketMessageFactory.CreateConnectMessage(username);
            Logger.Info($"Sending connect message [{connectMessage}]");
            return ws.Send(connectMessage);
        }

        public void Dispose()
        {
            ws?.Dispose();
        }

        private Task OnOpen()
        {
            return Task.Run(() => Logger.Info($"Websocket connection [{webSocketEndPoint}] is now open"));
        }

        private Task OnMessage(MessageEventArgs arg)
        {
            return Task.Run(() =>
            {
                var jsonResponseString = Regex.Unescape(arg.Text.ReadToEnd());
                Logger.Info($"Socket message received: [{jsonResponseString}]");
                var jsonResponse = JObject.Parse(jsonResponseString);
                var messageType = jsonResponse["messageType"].Value<string>();

                if (messageType.Equals(SocketMessageType.ConnectedUsers.Value))
                {
                    var users = JsonConvert.DeserializeObject<IEnumerable<User>>(jsonResponse["data"].ToString());
                    eventAggregator.GetEvent<ConnectedUsersEvent>().Publish(users.ToList());
                }
                else
                {
                    Logger.Error($"Invalid socket message type: [{messageType}]");
                }
            });
        }

        private Task OnError(ErrorEventArgs arg)
        {
            return Task.Run(() => Logger.Error($"WebSocket error occurred: [{arg.Message}]"));
        }

        private Task OnClose(CloseEventArgs arg)
        {
            return Task.Run(() => Logger.Info($"Websocket connection [{webSocketEndPoint}] is now closed"));
        }
    }
}