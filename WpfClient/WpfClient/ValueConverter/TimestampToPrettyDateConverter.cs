using System;
using System.Globalization;
using System.Windows.Data;

namespace WpfClient.ValueConverter
{
    public class TimestampToPrettyDateConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var dateTime = DateTimeOffset.FromUnixTimeMilliseconds(long.Parse(value.ToString())).LocalDateTime;
            return String.Format("{0:ddd, MMM d, yyyy HH:mm}", dateTime);
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}