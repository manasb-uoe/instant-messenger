using System;
using System.Threading.Tasks;
using WpfClient.Model;

namespace WpfCLient.DataAccess
{
    public interface IChatSocketApi : IDisposable
    {
        void OpenSocketConnection();
        Task<bool> SendConnectMessage(string username);
        Task<bool> SendChatMessage(User user, string messageText);
    }
}