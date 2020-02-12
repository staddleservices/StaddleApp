package staddle.com.staddle.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import staddle.com.staddle.R;

public class NotificationNewActivity extends AppCompatActivity {
    Button noti;
    int counter = 0;
    boolean ncompi = false;

    NotificationManager nManager;
    NotificationCompat.Builder ncomp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
       // editor.putInt("count", count);
        editor.putInt("counter", counter);
        editor.commit();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int i = sharedPreferences.getInt("count", 0);
        int j = sharedPreferences.getInt("counter", 0);


        TextView txt_bell_count =  findViewById(R.id.txt_bell_count);
        txt_bell_count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                ncomp = new NotificationCompat.Builder(NotificationNewActivity.this);
                ncomp.setContentTitle("9999900000");
                if (counter == 0) {
                //    ncomp.setContentText(ss);
                    counter++;
                    // ncomp.setContentText(ss);
                    Intent resultIntent = new Intent(NotificationNewActivity.this, NotificationActivity.class);

                    //    resultIntent.putExtra("msg", ss);
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 1, resultIntent, 0);

                    ncomp.setContentIntent(pi);
                    ncomp.setTicker("Notification Listener");
                    ncomp.setSmallIcon(R.drawable.ic_launcher_background);

                    ncomp.setAutoCancel(true);

                  //  nManager.notify(NOTIFY_ID, ncomp.build());
                } else {
                    ncomp.setContentTitle("Notification");
                    ncomp.setContentText(counter + " Notifications");
                    Intent resultIntent = new Intent(NotificationNewActivity.this, OfferAndPromoActivity.class);

                    //  resultIntent.putExtra("message", ss);
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 1, resultIntent, 0);

                    ncomp.setContentIntent(pi);
                    ncomp.setTicker("Notification Listener");
                    ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                    ncomp.setAutoCancel(true);
                   // nManager.notify(NOTIFY_ID, ncomp.build());

                    counter++;
                }
            }
        });
    }
}



