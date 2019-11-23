package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.EditProfileResponse;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

public class SettingActivity extends AppCompatActivity {
    ImageView iv_back;
    ExpandableRelativeLayout exp_rel_one;
    ImageView iv_version_arrow;
    TextView tv_update_profile;
    RelativeLayout rl_old_password, rl_new_con_new_password, rl_con_new_password, rl_forgot_password, rl_button_save;
    LinearLayout ll_change_password;
    EditText et_tvEstabl_old_password, et_tvEstabl_new_password, et_tvEstabl_con_new_password;
    ApiInterface apiInterface;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        AppConstants.ChangeStatusBarColor(SettingActivity.this);

        find_All_Ids();
        userId = AppPreferences.loadPreferences(SettingActivity.this, "USER_ID");

        exp_rel_one.collapse();

        iv_version_arrow.setImageResource(R.drawable.ic_vector_circle_left_arrow);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rl_old_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   txt_select_establish_spn_ind.setText(tvEstabl_partnership.getText().toString());
                getExpandOrCollapseEstabilsh(exp_rel_one);
            }
        });

        rl_new_con_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExpandOrCollapseEstabilsh(exp_rel_one);
            }
        });

        rl_con_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExpandOrCollapseEstabilsh(exp_rel_one);
            }
        });


        rl_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExpandOrCollapseEstabilsh(exp_rel_one);
            }
        });

        ll_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExpandOrCollapseEstabilsh(exp_rel_one);
            }
        });
//=================================
        rl_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        tv_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    } // ======================= End Of onCreate() ==========================

    private void find_All_Ids() {
        iv_back = findViewById(R.id.iv_back);
        exp_rel_one = findViewById(R.id.exp_rel_one);
        iv_version_arrow = findViewById(R.id.iv_version_arrow);
        et_tvEstabl_old_password = findViewById(R.id.et_tvEstabl_old_password);
        et_tvEstabl_new_password = findViewById(R.id.et_tvEstabl_new_password);
        et_tvEstabl_con_new_password = findViewById(R.id.et_tvEstabl_con_new_password);
        rl_old_password = findViewById(R.id.rl_old_password);
        rl_new_con_new_password = findViewById(R.id.rl_new_con_new_password);
        rl_con_new_password = findViewById(R.id.rl_con_new_password);
        rl_button_save = findViewById(R.id.rl_button_save);
        ll_change_password = findViewById(R.id.ll_change_password);
        rl_forgot_password = findViewById(R.id.rl_forgot_password);
        tv_update_profile = findViewById(R.id.tv_update_profile);
    }

    private void getExpandOrCollapseEstabilsh(ExpandableRelativeLayout exp_rel_one) {
        if (!exp_rel_one.isExpanded()) {
            et_tvEstabl_old_password.setText("");
            et_tvEstabl_new_password.setText("");
            et_tvEstabl_con_new_password.setText("");
            iv_version_arrow.setImageResource(R.drawable.ic_vector_circle_down_arrow);
        } else {
            iv_version_arrow.setImageResource(R.drawable.ic_vector_circle_left_arrow);
        }
        exp_rel_one.toggle();

        doValidation();

    }

    private void doValidation() {

        String login_status = AppPreferences.loadPreferences(SettingActivity.this, "LOGIN_STATUS");
        if (login_status.equalsIgnoreCase("1")) {

            if (et_tvEstabl_old_password.getText().toString().trim().length() == 0) {
                et_tvEstabl_old_password.requestFocus();

            } else if (et_tvEstabl_old_password.getText().toString().trim().length() < 5) {

                final Toast toast = Toast.makeText(SettingActivity.this,
                        "Please enter at least 5 digits in password", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (et_tvEstabl_old_password.getText().toString().trim().length() > 15) {

                final Toast toast = Toast.makeText(SettingActivity.this,
                        "Password maximum length should be 15", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (et_tvEstabl_new_password.getText().toString().trim().length() == 0) {
                final Toast toast = Toast.makeText(SettingActivity.this,
                        "Please enter new password", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (et_tvEstabl_new_password.getText().toString().trim().length() < 5) {

                final Toast toast = Toast.makeText(SettingActivity.this,
                        "Please enter at least 5 digits in  new password", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (et_tvEstabl_new_password.getText().toString().trim().length() > 15) {

                final Toast toast = Toast.makeText(SettingActivity.this, "Password maximum length should be 15", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else if (!et_tvEstabl_new_password.getText().toString().equalsIgnoreCase(et_tvEstabl_con_new_password.getText().toString())) {
                Toast.makeText(SettingActivity.this, "please re-enter correct password", Toast.LENGTH_LONG).show();

            } else {
                String curentpassword = et_tvEstabl_old_password.getText().toString();
                String newPass = et_tvEstabl_new_password.getText().toString();
                String id = AppPreferences.loadPreferences(SettingActivity.this, "User_ID");
                if (CheckNetwork.isNetworkAvailable(SettingActivity.this)) {
                    // ======== Api Call =============
                    changePassword(userId, curentpassword, newPass);
                    et_tvEstabl_old_password.setText("");
                    et_tvEstabl_new_password.setText("");
                    et_tvEstabl_con_new_password.setText("");
                } else {
                    Toast.makeText(SettingActivity.this, "Check your internet connection !", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
            startActivity(intent);
            SettingActivity.this.finish();
        }
    }

    private void changePassword(String uid, String password, String newpassword) {

        final ProgressDialog progressDialog = new ProgressDialog(SettingActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<EditProfileResponse> call = apiInterface.changePassword(uid, password, newpassword);
        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                progressDialog.dismiss();
                String str_response = new Gson().toJson(response.body());
                //Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {

                        EditProfileResponse editProfileResponse = response.body();

                        if (editProfileResponse != null) {
                            String status = editProfileResponse.getStatus();
                            String message = editProfileResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                Toast.makeText(SettingActivity.this, "" + message, Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(SettingActivity.this, "" + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                Toast.makeText(SettingActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
