package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.FavouriteListResponse;
import staddle.com.staddle.adapter.FavouriteListAdapter;
import staddle.com.staddle.bean.FavouriteListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;

public class MyWishListActivity extends AppCompatActivity {
    RecyclerView rv_myWishList;
    ImageView iv_back;
    RelativeLayout rl_no_fav;
    ArrayList<FavouriteListModel> favouriteListModelslist;
    FavouriteListAdapter favouriteListAdapter;
    String userId;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_list);
        AppConstants.ChangeStatusBarColor(MyWishListActivity.this);

        find_All_Ids();

        favouriteListModelslist = new ArrayList<>();
        userId = AppPreferences.loadPreferences(MyWishListActivity.this, "USER_ID");

        if (staddle.com.staddle.utils.CheckNetwork.isNetworkAvailable(MyWishListActivity.this)) {
            getFavouriteList(userId);
        } else {
            Alerts.showAlert(MyWishListActivity.this);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void find_All_Ids() {
        rv_myWishList = findViewById(R.id.rv_myWishList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_myWishList.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        iv_back = findViewById(R.id.iv_back);
        rl_no_fav = findViewById(R.id.rl_no);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void getFavouriteList(String uid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<FavouriteListResponse> call = apiInterface.getFavouriteList(uid);

        call.enqueue(new Callback<FavouriteListResponse>() {
            @Override
            public void onResponse(@NonNull Call<FavouriteListResponse> call, @NonNull Response<FavouriteListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        favouriteListModelslist.clear();
                        FavouriteListResponse favouriteListResponse = response.body();
                        if (favouriteListResponse != null) {
                            String status = favouriteListResponse.getStatus();
                            String message = favouriteListResponse.getMessage();
                            if (status.equalsIgnoreCase("1")) {
                                favouriteListModelslist = favouriteListResponse.getInfo();
                                favouriteListAdapter = new FavouriteListAdapter(MyWishListActivity.this, favouriteListModelslist);
                                rv_myWishList.setAdapter(favouriteListAdapter);
                                rv_myWishList.setVisibility(View.VISIBLE);
                                rl_no_fav.setVisibility(View.GONE);
                                rv_myWishList.setHasFixedSize(true);
                                favouriteListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MyWishListActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                rl_no_fav.setVisibility(View.VISIBLE);
                                rv_myWishList.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(MyWishListActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<FavouriteListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MyWishListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
