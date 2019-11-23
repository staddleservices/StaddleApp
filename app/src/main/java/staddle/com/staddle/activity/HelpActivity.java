package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.AddFavouriteResponse;
import staddle.com.staddle.ResponseClasses.MyHelpListResponse;
import staddle.com.staddle.adapter.MyHelpListAdapter;
import staddle.com.staddle.bean.MyHelpList;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;
import staddle.com.staddle.utils.CheckNetwork;

public class HelpActivity extends AppCompatActivity {

    ImageView iv_back;
    EditText edt_comment;
    Button btn_send;
    ProgressDialog pd;
    ApiInterface apiInterface;
    String userId, comment;
    RecyclerView rv_myMyHelp;
    ArrayList<MyHelpList> myOrderListModelArrayList;
    MyHelpListAdapter myOrderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        AppConstants.ChangeStatusBarColor(HelpActivity.this);

        find_All_ID();

        userId = AppPreferences.loadPreferences(HelpActivity.this, "USER_ID");

        if (CheckNetwork.isNetworkAvailable(this)) {
            getMyHelpListUser(userId);
        } else {
            Alerts.showAlert(this);
        }

        iv_back.setOnClickListener(view -> onBackPressed());

        btn_send.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt_comment.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            comment = edt_comment.getText().toString().trim();
            if (!comment.equalsIgnoreCase("")) {
                getHelp(userId, comment);
            } else {
                Toast.makeText(this, "Please enter any comment", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void find_All_ID() {

        myOrderListModelArrayList = new ArrayList<>();
        iv_back = findViewById(R.id.iv_back);
        edt_comment = findViewById(R.id.edt_comment);
        btn_send = findViewById(R.id.btn_send);
        rv_myMyHelp = findViewById(R.id.rv_myMyHelp);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_myMyHelp.setLayoutManager(linearLayoutManager);
    }

    private void getMyHelpListUser(String uid) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MyHelpListResponse> call = apiInterface.getMyHelpListUser(uid);

        call.enqueue(new Callback<MyHelpListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyHelpListResponse> call, @NonNull Response<MyHelpListResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        myOrderListModelArrayList.clear();
                        MyHelpListResponse myOrderListResponse = response.body();
                        if (myOrderListResponse != null) {
                            String status = myOrderListResponse.getStatus();
                            String message = myOrderListResponse.getMessage();
                            if (status.equalsIgnoreCase("1")) {
                                myOrderListModelArrayList = myOrderListResponse.getInfo();
                                myOrderListAdapter = new MyHelpListAdapter(HelpActivity.this, myOrderListModelArrayList);
                                rv_myMyHelp.setAdapter(myOrderListAdapter);
                                myOrderListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(HelpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(HelpActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyHelpListResponse> call, Throwable t) {
                Toast.makeText(HelpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getHelp(String userId, String mes) {
        pd = new ProgressDialog(HelpActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AddFavouriteResponse> call = apiInterface.help(userId, mes);

        call.enqueue(new Callback<AddFavouriteResponse>() {
            @Override
            public void onResponse(Call<AddFavouriteResponse> call, final Response<AddFavouriteResponse> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {
                        comment = "";
                        edt_comment.setText("");
                        AddFavouriteResponse responsee = response.body();
                        if (responsee != null) {
                            String message = responsee.getMessage();
                            int status = responsee.getStatus();
                            if (status == 1) {
                                getMyHelpListUser(userId);
                                Toast.makeText(HelpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HelpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddFavouriteResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(HelpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}