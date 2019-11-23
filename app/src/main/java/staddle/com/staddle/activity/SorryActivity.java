package staddle.com.staddle.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import staddle.com.staddle.R;
import staddle.com.staddle.utils.AppConstants;


public class SorryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorry);

        AppConstants.ChangeStatusBarColor(SorryActivity.this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
