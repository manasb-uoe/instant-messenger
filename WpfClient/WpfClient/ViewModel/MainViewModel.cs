namespace WpfClient.ViewModel
{
    public class MainViewModel
    {
        public string Greeting { get; private set; }

        public MainViewModel() {
            Greeting = "Hello, world!";
        }
    }
}