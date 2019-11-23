package staddle.com.staddle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import staddle.com.staddle.R;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

public class AtHomeActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView txt_athome, txt_atSalon, txt_men, txt_women, txt_mens, txt_womens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athome);

        AppConstants.ChangeStatusBarColor(AtHomeActivity.this);

        find_All_ID();

        iv_back.setOnClickListener(view -> onBackPressed());

        txt_men.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Men");
            intent.putExtra("SubCat", "At Home");
            intent.putExtra("Tag", "AtHomeMen");
            startActivity(intent);
        });

        txt_women.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Women");
            intent.putExtra("SubCat", "At Home");
            intent.putExtra("Tag", "AtHomeMen");
            startActivity(intent);

        });

        txt_mens.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Men");
            intent.putExtra("SubCat", "At Salon");
            startActivity(intent);

        });

        txt_womens.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Women");
            intent.putExtra("SubCat", "At Salon");
            startActivity(intent);
        });
    }

    private void find_All_ID() {
        iv_back = findViewById(R.id.iv_back);
        txt_athome = findViewById(R.id.txt_athome);
        txt_atSalon = findViewById(R.id.txt_atSalon);
        txt_men = findViewById(R.id.txt_men);
        txt_women = findViewById(R.id.txt_women);
        txt_mens = findViewById(R.id.txt_mens);
        txt_womens = findViewById(R.id.txt_womens);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
