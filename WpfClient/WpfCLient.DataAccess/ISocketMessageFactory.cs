namespace WpfCLient.DataAccess
{
    public interface ISocketMessageFactory
    {
        string CreateConnectMessage(string username);
    }
}