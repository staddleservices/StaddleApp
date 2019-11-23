package staddle.com.staddle.sheardPref;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String PREFS_NAME = "MyPrefs";

    public static String savePreferences(Context context, String key, String value)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(key, value);
        editor.apply();

        return key;
    }

    public static String loadPreferences(Context context, String key) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String value = settings.getString(key, "");

        return value;
    }

    public static String savePreferencesCount(Context context, String key, int value)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();

        return key;
    }

    public static int loadPreferencesCount(Context context, String key) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int value = settings.getInt(key, 1);

        return value;
    }
}
