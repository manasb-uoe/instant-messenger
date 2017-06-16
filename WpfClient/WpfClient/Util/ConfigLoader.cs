using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace WpfClient.Util
{
    class ConfigLoader
    {
        private string path;

        public ConfigLoader(string path)
        {
            this.path = path;
        }

        public Config Load()
        {
            using(var streamReader = new StreamReader(path))
            {
                string json = streamReader.ReadToEnd();
                var config = JsonConvert.DeserializeObject<Config>(json);

                return config;
            }
        }
    }
}
