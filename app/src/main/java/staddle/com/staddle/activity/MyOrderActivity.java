package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.MyOrderListResponse;
import staddle.com.staddle.adapter.MyOrderListAdapter;
import staddle.com.staddle.bean.MyOrderListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;
import staddle.com.staddle.utils.CheckNetwork;

import static android.graphics.Color.TRANSPARENT;
import static staddle.com.staddle.sheardPref.AppPreferences.PREFS_NAME;

public class MyOrderActivity extends AppCompatActivity {

    RadioGroup toggle;
    RadioButton delivered, ordered;
    ImageView iv_back;
    RecyclerView rv_myOrderList;
    String userId;
    ApiInterface apiInterface;
    ArrayList<MyOrderListModel> myOrderListModelArrayList;
    MyOrderListAdapter myOrderListAdapter;
    RelativeLayout rl_no;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        AppConstants.ChangeStatusBarColor(MyOrderActivity.this);
        userId = AppPreferences.loadPreferences(MyOrderActivity.this, "USER_ID");

        find_All_Ids();

        if (CheckNetwork.isNetworkAvailable(MyOrderActivity.this)) {
            getMyOrderList(userId);
        } else {
            Alerts.showAlert(MyOrderActivity.this);
        }

        iv_back.setOnClickListener(view -> onBackPressed());
    }

    private void find_All_Ids() {
        myOrderListModelArrayList = new ArrayList<>();

        iv_back = findViewById(R.id.iv_back);
        toggle = findViewById(R.id.toggle);
        delivered = findViewById(R.id.search);
        ordered = findViewById(R.id.offer1);
        rl_no = findViewById(R.id.rl_no);
        rv_myOrderList = findViewById(R.id.rv_myOrderList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_myOrderList.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        myOrderListAdapter = new MyOrderListAdapter(MyOrderActivity.this, myOrderListModelArrayList);
        rv_myOrderList.setAdapter(myOrderListAdapter);
        rv_myOrderList.setHasFixedSize(true);
        myOrderListAdapter.notifyDataSetChanged();
    }

    private void getMyOrderList(String uid) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MyOrderListResponse> call = apiInterface.getMyOrderListUser(uid);

        call.enqueue(new Callback<MyOrderListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderListResponse> call, @NonNull Response<MyOrderListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        myOrderListModelArrayList.clear();
                        MyOrderListResponse myOrderListResponse = response.body();
                        if (myOrderListResponse != null) {
                            String status = myOrderListResponse.getStatus();
                            String message = myOrderListResponse.getMessage();
                            if (status.equalsIgnoreCase("1")) {
                                myOrderListModelArrayList = myOrderListResponse.getInfo();
                                myOrderListAdapter = new MyOrderListAdapter(MyOrderActivity.this, myOrderListModelArrayList);
                                rv_myOrderList.setAdapter(myOrderListAdapter);
                                rv_myOrderList.setVisibility(View.VISIBLE);
                                rl_no.setVisibility(View.GONE);
                                rv_myOrderList.setHasFixedSize(true);
                                myOrderListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MyOrderActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                rl_no.setVisibility(View.VISIBLE);
                                rv_myOrderList.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(MyOrderActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyOrderListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MyOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    public void ratingDialog(String oid) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        TextView btn_not_now = dialogView.findViewById(R.id.btn_not_now);
        TextView btn_yes = dialogView.findViewById(R.id.btn_yes);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

        btn_not_now.setOnClickListener(view -> alertDialog.dismiss());

        btn_yes.setOnClickListener(v -> {
            if ((int) ratingBar.getRating() == 0) {
                Toast.makeText(this, "Please select an rating.", Toast.LENGTH_SHORT).show();
            } else {
                alertDialog.dismiss();
                makeRating(oid, String.valueOf(ratingBar.getRating()));
            }

        });
    }

    private void makeRating(String oid, String rating) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MyOrderListResponse> call = apiInterface.makeRating(oid, rating);

        call.enqueue(new Callback<MyOrderListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderListResponse> call, @NonNull Response<MyOrderListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Toast.makeText(MyOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getMyOrderList(userId);
                    } else {
                        Toast.makeText(MyOrderActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyOrderListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MyOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}


