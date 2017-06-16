namespace WpfCLient.DataAccess
{
    public class ApiResponse<T>
    {
        public T Body { get; set; }
        public string Error { get; set; }

        public bool IsSuccessful()
        {
            return Error == null;
        }
    }
}