package staddle.com.staddle.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import staddle.com.staddle.R;

public class AppConstants {

    public static final String APP_BASE_URL = "http://staddle.in/mobileapp/api/";

//    ==============================================================================================

    @SuppressLint("ObsoleteSdkInt")
    public static void ChangeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.app_status_bar_color));
        }
    }
    public static final String PRIVACY_POLICY = APP_BASE_URL + "/wsGetcmsDetails.php?id=4";
}
