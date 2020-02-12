package staddle.com.staddle.activity;

import android.os.Bundle;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import staddle.com.staddle.R;
import staddle.com.staddle.utils.AppConstants;

public class AboutUsActivity extends AppCompatActivity {
    public String postUrl = "http://staddle.in/mobileapp/api/wsGetcmsDetails.php?id=3";
    public WebView webView;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        AppConstants.ChangeStatusBarColor(AboutUsActivity.this);

        find_All_ID();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);
    }

    private void find_All_ID() {
        iv_back = findViewById(R.id.iv_back);
        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
