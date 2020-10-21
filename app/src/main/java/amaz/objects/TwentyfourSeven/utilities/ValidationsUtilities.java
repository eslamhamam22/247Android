package amaz.objects.TwentyfourSeven.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ValidationsUtilities {

    public static boolean isValidName(String name) {
        if(name.matches("^[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z\\s]+[\\u0600-\\u065F\\u066A-\\u06EF\\u06FA-\\u06FFa-zA-Z-_]*$")) {
            return true;
        } else {
            return false;
        }
    }
    public static String reformatTime(String oldFormattedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 2018-04-16T15:17:24.230Z
        String newFormattedDate = "";
        try {
            Date date = format.parse(oldFormattedDate);
            newFormattedDate = convertDateAndTimeToString(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return newFormattedDate;
    }

    public static String reformatTimeOnly(String oldFormattedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 2018-04-16T15:17:24.230Z
        String newFormattedDate = "";
        try {
            Date date = format.parse(oldFormattedDate);
            newFormattedDate = convertDateInTimeOnly(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return newFormattedDate;
    }

    public static String reformatDateOnly(String oldFormattedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 2018-04-16T15:17:24.230Z
        String newFormattedDate = "";
        try {
            Date date = format.parse(oldFormattedDate);
            newFormattedDate = convertDateInDateOnly(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return newFormattedDate;
    }

    public static String reformatDateInProfile(String oldFormattedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 2018-04-16T15:17:24.230Z
        String newFormattedDate = "";
        try {
            Date date = format.parse(oldFormattedDate);
            newFormattedDate = convertDateInProfile(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return newFormattedDate;
    }

    public static String getDateFromChat(long timeStamp){
        String newFormattedDate = "";

        try{
            Date netDate = (new Date(timeStamp));
            newFormattedDate = convertDateInChat(netDate);

            return newFormattedDate;
        }
        catch(Exception ex){
            return newFormattedDate ;
        }
    }

    public static String convertDateAndTimeToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyy  h:mm a", Locale.ENGLISH);
        String dateString = format.format(date);
        return dateString;
    }

    public static String reformatTimeToTwoLines(String oldFormattedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        // 2018-04-16T15:17:24.230Z
        String newFormattedDate = "";
        try {
            Date date = format.parse(oldFormattedDate);
            newFormattedDate = convertDateAndTimeToTwoLinesString(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return newFormattedDate;
    }

    public static String convertDateAndTimeToTwoLinesString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyy\nh:mm a", Locale.ENGLISH);
        String dateString = format.format(date);
        return dateString;
    }

    public static String convertDateInChat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("h:mm a - dd MMM yyy", Locale.ENGLISH);
        String dateString = format.format(date);
        return dateString;
    }

    public static String convertDateInTimeOnly(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("h:mm a ", Locale.ENGLISH);
        String dateString = format.format(date);
        return dateString;
    }

    public static String convertDateInDateOnly(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM.yyy ", Locale.ENGLISH);
        String dateString = format.format(date);
        return dateString;
    }

    private static String convertDateInProfile(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String dateString = format.format(date);
        return dateString;
    }
}
