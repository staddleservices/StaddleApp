package staddle.com.staddle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.utils.AppConstants;

public class NotificationActivity extends AppCompatActivity {

    ImageView iv_back;
    RelativeLayout rv_booking_status, rv_wishlist_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        AppConstants.ChangeStatusBarColor(NotificationActivity.this);

        find_All_ID();

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}