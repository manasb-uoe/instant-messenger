using System;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using WpfClient.Model;
using WpfClient.Util;

namespace WpfCLient.DataAccess
{
    public class UserApi : IUserApi
    {
        private readonly Config config;
        private readonly IHttpHandler httpHandler;

        public UserApi(Config config, IHttpHandler httpHandler)
        {
            this.config = config;
            this.httpHandler = httpHandler;
        }

        public async Task<ApiResponse<User>> AddUserAsync(string username)
        {
            using (httpHandler)
            {
                var requestUri = config.ApiBaseUrl + "/api/add-user";
                var postContent = new StringContent(username);
                var response = await httpHandler.PostAsync(requestUri, postContent);
                var responseBody = await response.Content.ReadAsStringAsync();
                var json = JObject.Parse(responseBody);
                var statusCode = json["statusCode"].Value<int>();

                if (statusCode == 200)
                {
                    return new ApiResponse<User>()
                    {
                        Body = JsonConvert.DeserializeObject<User>(json["data"].ToString())
                    };
                }
                else
                {
                    return new ApiResponse<User>()
                    {
                        Error = json["data"]["error"].Value<string>()
                    };
                }
            }
        }
    }
}