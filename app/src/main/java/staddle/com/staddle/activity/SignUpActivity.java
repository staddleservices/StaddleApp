package staddle.com.staddle.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.linkedin.platform.LISessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.bean.MySingleton;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.ResponseClasses.SignUpResponse;
import staddle.com.staddle.retrofitApi.EndApi;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

import static android.graphics.Color.TRANSPARENT;
import static staddle.com.staddle.sheardPref.AppPreferences.PREFS_NAME;


public class SignUpActivity extends AppCompatActivity {
    ProgressDialog pd;
    String TAG = getClass().getSimpleName();
    Button btn_sign_up;
    EditText edt_name, edt_email_id;
    ApiInterface apiInterface;
    String mobile;
    String generatedUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent=getIntent();
        mobile=intent.getStringExtra("mobile_number");

        AppConstants.ChangeStatusBarColor(SignUpActivity.this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        find_All_IDs();

        btn_sign_up.setOnClickListener(view -> doValidation());
    }

    private void find_All_IDs() {
        btn_sign_up = findViewById(R.id.btnsbumitreg);
        edt_name = findViewById(R.id.edt_name);

        edt_email_id = findViewById(R.id.edt_email_id);

    }

    private void doValidation() {
        String username = edt_name.getText().toString().trim();

        String email = edt_email_id.getText().toString().trim();


        AppPreferences.savePreferences(SignUpActivity.this, "EMAIL", email);
        AppPreferences.savePreferences(SignUpActivity.this, "MOBILE", mobile);

        if (username.equalsIgnoreCase("")) {
            edt_name.setError("Enter Name");
            edt_name.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            edt_email_id.setError("Please Enter Email");
            edt_email_id.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_id.getText().toString()).matches()) {
            edt_email_id.setError("Invalid Email !!");
            edt_email_id.requestFocus();
        }else {
            if (CheckNetwork.isNetworkAvailable(SignUpActivity.this)) {
                // ======== Api Call =============
                doSignUp(username, email, "12345678", mobile);
            } else {
                Toast.makeText(SignUpActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doSignUp(String username, String email, String password, final String mobile) {
        pd = new ProgressDialog(SignUpActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<SignUpResponse> call = apiInterface.UserSignUp(username, email, password, mobile);

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, final Response<SignUpResponse> response) {
                pd.dismiss();
                Log.d("" + TAG, "" + response);

                int status = response.body().getStatus();
                String message = response.body().getMessage();
                int uid=response.body().getUid();
                Log.e("UID",String.valueOf(uid));
                generatedUid=String.valueOf(uid);

                switch (status) {
                    case 1:
                        signUp(username,email,password, mobile,generatedUid);
                        break;
                    case 2:
                        Toast.makeText(SignUpActivity.this, message+"!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(SignUpActivity.this, message+"!!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(SignUpActivity.this, t.getMessage()+"!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LocationActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

    }

    public void upload_token(String uid,String token){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndApi.UPLOAD_TOKEN,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.e("response",response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //singupStatus = jsonObject.getString("status");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("responce", error.toString() );
                new AlertDialog.Builder(SignUpActivity.this)
                        .setTitle("Connection failed!")
                        .setCancelable(false)
                        .setMessage("Please check your internet connection or restart the App!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("token",token);
                params.put("uid",uid);



                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void signUp(String username, String email, String password, String mobile, String uid) {
          String device_token = AppPreferences.loadPreferences(SignUpActivity.this, "DEVICE_TOKEN");
          upload_token(uid,device_token);
          Intent intent = new Intent(SignUpActivity.this, LocationActivity.class);
          intent.putExtra("USER_ID",uid);
          intent.putExtra("USER_NAME",email);
          intent.putExtra("USER_EMAIL",password);
          intent.putExtra("USER_PROFILE_PIC","");
          startActivity(intent);
          finish();
          overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
//        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.log_out_box, null);
//        builder.setView(dialogView);
//        builder.setCancelable(false);
//
//        final AlertDialog alertDialog = builder.create();
//        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
//        alertDialog.show();
//        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
//        alertDialog.getWindow().setGravity(Gravity.CENTER);
//
//        TextView btn_not_now = dialogView.findViewById(R.id.btn_not_now);
//        TextView tv_delete_confirm_text = dialogView.findViewById(R.id.tv_delete_confirm_text);
//        TextView tv_delete_post = dialogView.findViewById(R.id.tv_delete_post);
//        TextView btn_yes = dialogView.findViewById(R.id.btn_yes);
//        btn_yes.setText("Ok");
//        btn_not_now.setVisibility(View.GONE);
//        tv_delete_post.setText("Sign Up");
//        tv_delete_confirm_text.setText(message);
//
//        btn_yes.setOnClickListener(v -> {
//            alertDialog.dismiss();
//            Intent intent = new Intent(SignUpActivity.this, LocationActivity.class);
//            intent.putExtra("mobile", mobile);
//            startActivity(intent);
//            finish();
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        });
    }

}
