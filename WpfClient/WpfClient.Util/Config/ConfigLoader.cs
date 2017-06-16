using System.IO;
using Newtonsoft.Json;

namespace WpfClient.Util.Config
{
    public class ConfigLoader
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
                var config = JsonConvert.DeserializeObject<Util.Config.Config>(json);

                return config;
            }
        }
    }
}
