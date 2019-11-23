package staddle.com.staddle.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import staddle.com.staddle.R;
import staddle.com.staddle.utils.AppConstants;

public class PolicyActivity extends AppCompatActivity {
    ImageView iv_back;
    public String postUrl = "http://staddle.in/mobileapp/api/wsGetcmsDetails.php?id=2";
    public WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        AppConstants.ChangeStatusBarColor(PolicyActivity.this);

        find_All_Ids();

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

    private void find_All_Ids() {
        iv_back = findViewById(R.id.iv_back);
        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
