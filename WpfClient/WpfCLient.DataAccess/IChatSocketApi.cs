using System;
using System.Threading.Tasks;

namespace WpfCLient.DataAccess
{
    public interface IChatSocketApi : IDisposable
    {
        void OpenSocketConnection();
        Task<bool> SendConnectMessage(string username);
    }
}