package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.ResponseClasses.ForgotPasswordResponse;
import staddle.com.staddle.utils.AppConstants;


public class ForgotPasswordActivity extends AppCompatActivity {

    ProgressDialog pd;
    ApiInterface apiInterface;

    LinearLayout ll_btn_submit;
    EditText edt_phone_number, edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        AppConstants.ChangeStatusBarColor(ForgotPasswordActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        find_All_IDs();

        ll_btn_submit.setOnClickListener(view -> doValidation());
    }

    private void doValidation() {
        String email = edt_email.getText().toString().trim();
        String number = edt_phone_number.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            edt_email.setError("Enter Email Id");
            edt_email.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Enter valid email id.");
            edt_email.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(ForgotPasswordActivity.this)) {
                forgotPassword(email);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forgotPassword(String email) {
        pd = new ProgressDialog(ForgotPasswordActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<ForgotPasswordResponse> call = apiInterface.forgotPassword(email);

        call.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("TAG", "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        ForgotPasswordResponse forgotPasswordResponse = response.body();

                        if (forgotPasswordResponse != null)
                        {
                            String status = forgotPasswordResponse.getStatus();
                            String message = forgotPasswordResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {
                                Toast.makeText(ForgotPasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "" + message, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                            }
                        }

                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response.code() + "Server Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {

            }
        });
    }

    private void find_All_IDs() {
        ll_btn_submit = findViewById(R.id.ll_btn_submit);
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_email = findViewById(R.id.edt_email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

    }
}
