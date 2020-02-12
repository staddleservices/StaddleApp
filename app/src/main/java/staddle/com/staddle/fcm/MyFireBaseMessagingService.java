package staddle.com.staddle.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.sheardPref.AppPreferences;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String CURRENT_ORDER_ID_KEY="CR_OR";
    public static final String CURRENT_ORDER_STATUS_KEY="CR_STS";
    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        AppPreferences.savePreferences(getApplicationContext(), "DEVICE_TOKEN", refreshedToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.

        // Check if message contains a data.
        if (remoteMessage.getData().size() > 0) {
            Log.d("ORDER_NOTIFICATION", "Message data payload: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String status=remoteMessage.getData().get("order_status");
            String order_id = remoteMessage.getData().get("order_id");
            sendNotification(remoteMessage.toString(),status,order_id);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("ORDER_NOTIFICATION", "Message Notification Body: " + remoteMessage.getData());
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String status=remoteMessage.getData().get("order_status");
            String order_id = remoteMessage.getData().get("order_id");
            sendNotification(body,status,order_id);
        }

    }

    private void sendNotification(String messageBody,String status,String order_id) {
        Intent intent = new Intent(this, HomeActivity.class);
        AppPreferences.savePreferences(getApplicationContext(),CURRENT_ORDER_ID_KEY,order_id);
        AppPreferences.savePreferences(getApplicationContext(),CURRENT_ORDER_STATUS_KEY,status);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, m, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("staddle.com.staddle", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "staddle.com.staddle")
                .setSmallIcon(R.drawable.app_logo)
                .setLargeIcon(largeIcon)
                .setContentTitle("Staddle")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationBuilder.setColor(getResources().getColor(R.color.transparent)).setLargeIcon(largeIcon);

        assert notificationManager != null;
        notificationManager.notify(m, notificationBuilder.build());

    }
}
