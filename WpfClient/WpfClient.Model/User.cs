using Newtonsoft.Json;

namespace WpfClient.Model
{
    public class User
    {
        [JsonProperty("username")]
        public string Username { get; set; }

        protected bool Equals(User other)
        {
            return string.Equals(Username, other.Username);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((User) obj);
        }

        public override int GetHashCode()
        {
            return (Username != null ? Username.GetHashCode() : 0);
        }

        public override string ToString()
        {
            return $"{nameof(Username)}: {Username}";
        }


    }
}