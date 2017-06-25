using WpfClient.Model;

namespace WpfCLient.DataAccess
{
    public interface ISocketMessageFactory
    {
        string CreateConnectMessage(string username);
        string CreateChatMessage(User user, string messageText);
    }
}