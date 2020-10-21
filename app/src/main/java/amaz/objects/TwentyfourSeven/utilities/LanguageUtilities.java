package amaz.objects.TwentyfourSeven.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageUtilities {

    public static void switchToArabicLocale(Context context){
        setLocale(context,Constants.ARABIC);
    }

    public static void switchToEnglishLocale(Context context){
        setLocale(context,Constants.ENGLISH);
    }

    private static void setLocale(Context context,String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        Resources resources = context.getResources();
        resources.updateConfiguration(config,resources.getDisplayMetrics());

    }


    public static String getLocale(){
        return Locale.getDefault().getLanguage();
    }
}
