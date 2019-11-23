package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import staddle.com.staddle.ResponseClasses.OffersAndPromoResponses;
import staddle.com.staddle.adapter.OfferAndPromoAdapter;
import staddle.com.staddle.bean.OfferAndPromoModel;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.utils.AppConstants;

public class OfferAndPromoActivity extends AppCompatActivity {
    ArrayList<OfferAndPromoModel> offerAndPromoArrayList;
    RecyclerView recyclerView;
    ImageView iv_back;
    ApiInterface apiInterface;
    RelativeLayout rl_no_details;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_and_promo);
        AppConstants.ChangeStatusBarColor(OfferAndPromoActivity.this);
        offerAndPromoArrayList = new ArrayList<>();

        find_All_ID();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (CheckNetwork.isNetworkAvailable(OfferAndPromoActivity.this)) {
            // ======== Api Call =============
            promoList();
        } else {
            Toast.makeText(OfferAndPromoActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void find_All_ID() {
        iv_back = findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.beautysalon_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OfferAndPromoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        rl_no_details = findViewById(R.id.rl_no_details);
    }
    //==============promoList APi========
    private void promoList() {
          progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<OffersAndPromoResponses> call = apiInterface.getOfferAndPromoList();

        call.enqueue(new Callback<OffersAndPromoResponses>() {
            @Override
            public void onResponse(@NonNull Call<OffersAndPromoResponses> call, @NonNull Response<OffersAndPromoResponses> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        offerAndPromoArrayList.clear();
                        OffersAndPromoResponses offersAndPromoResponses = response.body();
                        if (offersAndPromoResponses != null) {
                            String status = offersAndPromoResponses.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                offerAndPromoArrayList = offersAndPromoResponses.getInfo();

                                if (offerAndPromoArrayList != null) {
                                    OfferAndPromoAdapter offerAndPromoAdapter = new OfferAndPromoAdapter(OfferAndPromoActivity.this, offerAndPromoArrayList);
                                    recyclerView.setAdapter(offerAndPromoAdapter);
                                    recyclerView.setHasFixedSize(true);
                                    offerAndPromoAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(OfferAndPromoActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                recyclerView.setVisibility(View.VISIBLE);
                                rl_no_details.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(OfferAndPromoActivity.this, offersAndPromoResponses.getMessage(), Toast.LENGTH_SHORT).show();
                                rl_no_details.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                // productListCategoryModelArrayListt.clear();
                            }
                        }
                    } else {
                        Toast.makeText(OfferAndPromoActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<OffersAndPromoResponses> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OfferAndPromoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
