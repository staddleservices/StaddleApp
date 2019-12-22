package staddle.com.staddle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.adapter.NotificationsAdapter;
import staddle.com.staddle.bean.NotificationsDataModels;
import staddle.com.staddle.utils.AppConstants;

public class NotificationActivity extends AppCompatActivity {

    ImageView iv_back;
    RelativeLayout rv_booking_status, rv_wishlist_status;

    RecyclerView notifications;
    ArrayList<NotificationsDataModels>  notificationsDataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        AppConstants.ChangeStatusBarColor(NotificationActivity.this);

        find_All_ID();
        getnotifications("201");

        iv_back.setOnClickListener(view -> onBackPressed());

        rv_booking_status.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, MyOrderActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        });

        rv_wishlist_status.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, MyWishListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        });
    }

    private void find_All_ID() {
        iv_back = findViewById(R.id.iv_back);
        rv_booking_status = findViewById(R.id.rv_booking_status);
        rv_wishlist_status = findViewById(R.id.rv_wishlist_status);
        notifications = findViewById(R.id.notificationsRecycler);
        notificationsDataModels = new ArrayList<>();
        notifications.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void getnotifications(String uid){
//        notificationsDataModels.add(new NotificationsDataModels("Now save 20% on your first order!","New user? Get 20% off on your first order at any store.","0"));
//        notificationsDataModels.add(new NotificationsDataModels("Xyz Salon and Spa confirmed your order.","Your booking for 1 items at home at Xyz Salon and Spa has been confirmed.","0"));
//        notificationsDataModels.add(new NotificationsDataModels("Service Delivered!","Your booking for 1 items at home at Xyz Salon and Spa has been been delivered today at 2.30 pm at your home.Tap to rate Xyz Salon and Spa.","0"));
        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(NotificationActivity.this,notificationsDataModels);
        notifications.setAdapter(notificationsAdapter);
        notifications.hasFixedSize();

    }
}