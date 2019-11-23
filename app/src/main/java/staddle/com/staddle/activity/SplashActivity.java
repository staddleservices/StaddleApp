package staddle.com.staddle.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppConstants.ChangeStatusBarColor(SplashActivity.this);

        CheckPermissions();
    }

    void CheckPermissions() {
        RxPermissions.getInstance(SplashActivity.this)
                .request(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .subscribe(this::initialize, throwable -> {
                });
    }

    public void initialize(boolean isAppInitialized) {
        if (isAppInitialized) {
            Thread background = new Thread() {
                public void run() {
                    try {
                        sleep(1500);

                        String login_status = AppPreferences.loadPreferences(SplashActivity.this, "LOGIN_STATUS");
                        if (login_status.equalsIgnoreCase("1")) {
                            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();
                        } else {
                            Intent i = new Intent(SplashActivity.this, MobileLoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            background.start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("All permissions necessary");
            builder.setPositiveButton("Allow", (dialog, which) -> CheckPermissions());
            builder.setNegativeButton("Exit", (dialog, which) -> finish());
            builder.show();
        }
    }
}
