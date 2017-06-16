using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Moq;
using WpfClient.Model;
using WpfClient.Util;
using WpfCLient.DataAccess;
using Xunit;

namespace WpfClient.DataAccess.Tests
{
    public class UserApiTest
    {
        private readonly Mock<Config> configMock;
        private readonly Mock<IHttpHandler> httpHandlerMock;

        public UserApiTest()
        {
            configMock = new Mock<Config>();
            httpHandlerMock = new Mock<IHttpHandler>();
        }

        [Fact]
        public async void ShouldReturnUserIfAddUserRequestIsSuccessful()
        {
            var username = "TestUser";
            string sampleSuccessResponseString = "{\n    \"data\": {\n        \"username\": \"" + username + "\"\n    },\n    \"statusCode\": 200\n}";
            var httpResponseMessage = new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent(sampleSuccessResponseString)
            };
            httpHandlerMock.Setup(handler => handler.PostAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .Returns(Task.FromResult(httpResponseMessage));
            var userApi = new UserApi(configMock.Object, httpHandlerMock.Object);

            var response = await userApi.AddUserAsync(username);

            Assert.Equal(response.Body, new User() {Username = username});
            Assert.Null(response.Error);
            Assert.True(response.IsSuccessful());
        }

        [Fact]
        public async void ShouldReturnErrorIfAddUserRequestIsUnsuccessful()
        {
            const string errorMessage = "This is an error";;
            const string sampleErrorResponse = "{\n    \"data\": {\n        \"error\": \"" + errorMessage + "\"\n    },\n    \"statusCode\": 400\n}";
            var httpResponseMessage = new HttpResponseMessage(HttpStatusCode.BadRequest)
            {
                Content = new StringContent(sampleErrorResponse)
            };
            httpHandlerMock.Setup(handler => handler.PostAsync(It.IsAny<string>(), It.IsAny<HttpContent>()))
                .Returns(Task.FromResult(httpResponseMessage));
            var userApi = new UserApi(configMock.Object, httpHandlerMock.Object);

            var response = await userApi.AddUserAsync(string.Empty);

            Assert.Equal(response.Error, errorMessage);
            Assert.Null(response.Body);
            Assert.False(response.IsSuccessful());
            
        }
    }
}
