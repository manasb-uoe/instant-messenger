using System;
using System.Net.Http;
using System.Threading.Tasks;

namespace WpfCLient.DataAccess
{
    public interface IHttpHandler : IDisposable
    {
        Task<HttpResponseMessage> PostAsync(string requestUri, HttpContent postContent);
    }

    public class HttpHandler : IHttpHandler
    {
        private readonly HttpClient httpClient;

        public HttpHandler()
        {
            this.httpClient = new HttpClient();
        }

        public Task<HttpResponseMessage> PostAsync(string requestUri, HttpContent postContent)
        {
            return httpClient.PostAsync(requestUri, postContent);
        }

        public void Dispose()
        {
            httpClient.Dispose();
        }
    }
}