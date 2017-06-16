using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WpfClient.Model;

namespace WpfCLient.DataAccess
{
    public interface IUserApi
    {
        Task<ApiResponse<User>> AddUserAsync(string username);
    }
}
