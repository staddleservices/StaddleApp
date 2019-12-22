package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import staddle.com.staddle.R;
import staddle.com.staddle.utils.AppConstants;

public class TermsAndConditionActivity extends AppCompatActivity {
    LinearLayout ll_next, ll_check_checkBox;
    CheckBox checkBox;
    WebView webView;
    ProgressDialog pd;
    String privacy_policy_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        AppConstants.ChangeStatusBarColor(TermsAndConditionActivity.this);

        find_All_IDs();

        ll_check_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }
        });

        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(TermsAndConditionActivity.this, "Select terms and conditions", Toast.LENGTH_LONG).show();
                    checkBox.requestFocus();
                } else {
                    Intent intent = new Intent(TermsAndConditionActivity.this, LocationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                }
            }
        });

        //=========== Api Call ===============
        privacy_policy_url = AppConstants.PRIVACY_POLICY;

        pd = ProgressDialog.show(TermsAndConditionActivity.this, "", "Loading Please Wait...", true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//            webView.getSettings().setSupportZoom(true);
//            webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
        webView.loadUrl(privacy_policy_url);


    } // ========================== End Of OnCreateView() ===========================

    private void find_All_IDs() {
        ll_next = findViewById(R.id.ll_next);
        ll_check_checkBox = findViewById(R.id.ll_check_checkBox);
        checkBox = findViewById(R.id.checkbox);
        webView = findViewById(R.id.wv_term_condition);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
