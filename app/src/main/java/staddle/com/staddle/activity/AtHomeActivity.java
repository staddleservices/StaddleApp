package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.ProductListCategoryResponse;
import staddle.com.staddle.ResponseClasses.SubcategoryTreeListResponse;
import staddle.com.staddle.adapter.CategoryDetailsAdpater;
import staddle.com.staddle.adapter.FiltersAdapter;
import staddle.com.staddle.adapter.SubCategoryDetailsAdapter;
import staddle.com.staddle.bean.FilterSubCategoryProductListModel;
import staddle.com.staddle.bean.FiltersDataModel;
import staddle.com.staddle.bean.ProductListCategoryModel;
import staddle.com.staddle.bean.SubcategoryTreeListModel;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class AtHomeActivity extends AppCompatActivity {
    ImageView iv_back;
    TextView txt_athome, txt_atSalon, txt_men, txt_women, txt_mens, txt_womens;
    RecyclerView filters;
    RecyclerView vendor_list_recyclerview;
    ArrayList<FiltersDataModel> filtersDataModels;
    private ProgressDialog pd;
    ApiInterface apiInterface;
    ArrayList<SubcategoryTreeListModel> subcategoryTreeListModelArrayList;
    SubCategoryDetailsAdapter subCategoryDetailsAdapter;
    ArrayList<ProductListCategoryModel> productListCategoryModelArrayListt;
    CategoryDetailsAdpater categoryDetailsAdpater;
    private String isMenWomen = "", subCat = "", tag = "";
    String cid="";
    private TextView tv_action_bar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athome);

        AppConstants.ChangeStatusBarColor(AtHomeActivity.this);

        find_All_ID();

        //loadFilters();
        Toast.makeText(this, cid+" Y", Toast.LENGTH_LONG).show();
        loadfilteredVendors(cid);


        iv_back.setOnClickListener(view -> onBackPressed());

        txt_men.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Men");
            intent.putExtra("SubCat", "At Home");
            intent.putExtra("Tag", "AtHomeMen");
            startActivity(intent);
        });

        txt_women.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Women");
            intent.putExtra("SubCat", "At Home");
            intent.putExtra("Tag", "AtHomeMen");
            startActivity(intent);

        });

        txt_mens.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Men");
            intent.putExtra("SubCat", "At Salon");
            startActivity(intent);

        });

        txt_womens.setOnClickListener(view -> {
            Intent intent = new Intent(AtHomeActivity.this, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", "1");
            intent.putExtra("isMenWomen", "Women");
            intent.putExtra("SubCat", "At Salon");
            startActivity(intent);
        });
    }

    public void loadfilteredVendors(String cid) {
        Toast.makeText(this, cid+" X", Toast.LENGTH_LONG).show();
        switch (Integer.parseInt(cid)){
            case 1:
                subCategoryTreeDetailsListt(cid);
                break;

            case 2:
                subCategoryTreeDetailsListt(cid);
                break;

            case 3:
                subCategoryTreeDetailsListt(cid);
                break;

            case 4:
                subCategoryTreeDetailsListt(cid);
                break;
        }

    }

//    private void loadFilters() {
//        filtersDataModels.add(new FiltersDataModel("Only Men"));
//        filtersDataModels.add(new FiltersDataModel("Only Women"));
//        filtersDataModels.add(new FiltersDataModel("Top Rated"));
//        filtersDataModels.add(new FiltersDataModel("Near You"));
//        filtersDataModels.add(new FiltersDataModel("Only Home"));
//        filtersDataModels.add(new FiltersDataModel("At Salon"));
//        SubCategoryDetailsAdapter filtersAdapter = new SubCategoryDetailsAdapter(this,)
//        filters.setAdapter(filtersAdapter);
//        filters.hasFixedSize();
//
//
//    }

    private void find_All_ID() {
        iv_back = findViewById(R.id.iv_back);
        txt_athome = findViewById(R.id.txt_athome);
        txt_atSalon = findViewById(R.id.txt_atSalon);
        txt_men = findViewById(R.id.txt_men);
        txt_women = findViewById(R.id.txt_women);
        txt_mens = findViewById(R.id.txt_mens);
        txt_womens = findViewById(R.id.txt_womens);
        filters=findViewById(R.id.vendorfilters);
        tv_action_bar_title = findViewById(R.id.tv_action_bar_title);
        vendor_list_recyclerview = findViewById(R.id.filteredvendors);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        filters.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vendor_list_recyclerview.setLayoutManager(layoutManager2);
        filtersDataModels = new ArrayList<>();
        subcategoryTreeListModelArrayList = new ArrayList<>();
        productListCategoryModelArrayListt = new ArrayList<>();
        Intent intent1 = getIntent();
        cid = intent1.getStringExtra("cid");
        isMenWomen = intent1.getStringExtra("isMenWomen");
        subCat = intent1.getStringExtra("SubCat");
        tag = intent1.getStringExtra("Tag");
        switch (Integer.parseInt(cid)){
            case 1:
                tv_action_bar_title.setText("Beauty Salon");
                break;
            case 2:
                tv_action_bar_title.setText("House Keeping");
                break;
            case 3:
                tv_action_bar_title.setText("Security");
                break;
            case 4:
                tv_action_bar_title.setText("Spa");
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

    public void subCategoryTreeDetailsListt(String cid) {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait..."); // set message
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<SubcategoryTreeListResponse> call = apiInterface.getSubCategoryTreeDetails(cid);

        call.enqueue(new Callback<SubcategoryTreeListResponse>() {
            @Override
            public void onResponse(@NonNull Call<SubcategoryTreeListResponse> call, @NonNull Response<SubcategoryTreeListResponse> response) {
                pd.dismiss();
                try {
                    if (response.isSuccessful()) {
                        subcategoryTreeListModelArrayList.clear();
                        SubcategoryTreeListResponse subcategoryTreeListResponse = response.body();
                        if (subcategoryTreeListResponse != null) {
                            String status = subcategoryTreeListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                subcategoryTreeListModelArrayList = subcategoryTreeListResponse.getInfo();

                                if (subcategoryTreeListModelArrayList != null) {
                                    subCategoryDetailsAdapter = new SubCategoryDetailsAdapter(AtHomeActivity.this, subcategoryTreeListModelArrayList);
                                    filters.setAdapter(subCategoryDetailsAdapter);
                                    filters.setVisibility(View.VISIBLE);
                                    //rl_no_details.setVisibility(View.GONE);
                                    subCategoryDetailsAdapter.notifyDataSetChanged();

                                    subCategoryDetailsAdapter.setOnItemClickListener(subcategoryTreeListModel -> {
                                        productListcategory(subcategoryTreeListModel.getCid(), subcategoryTreeListModel.getSub_name());
                                    });
                                } else {
                                    //rl_no_details.setVisibility(View.VISIBLE);
                                    filters.setVisibility(View.GONE);
                                }
                            }
                            productListcategory(cid, "");
                        }
                    } else {
                        Toast.makeText(AtHomeActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubcategoryTreeListResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(AtHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void productListcategory(String cid, String sid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Please Wait..."); // set message
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductListCategoryResponse> call;

        String currLat = AppPreferences.loadPreferences(this, "LATTITUDE");
        String currLng = AppPreferences.loadPreferences(this, "LONGITUDE");

//        if (cid.equalsIgnoreCase("1"))
        call = apiInterface.getProductList(cid, sid, currLat, currLng, isMenWomen, subCat);
//        else
//            call = apiInterface.getProductList(cid, sid, "0", "0", "","");

        call.enqueue(new Callback<ProductListCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListCategoryResponse> call, @NonNull Response<ProductListCategoryResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        productListCategoryModelArrayListt.clear();
                        ProductListCategoryResponse productListCategoryResponse = response.body();
                        if (productListCategoryResponse != null) {
                            String status = productListCategoryResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                productListCategoryModelArrayListt = productListCategoryResponse.getInfo();

                                if (productListCategoryModelArrayListt != null) {

                                    categoryDetailsAdpater = new CategoryDetailsAdpater(AtHomeActivity.this, productListCategoryModelArrayListt);
                                    vendor_list_recyclerview.setAdapter(categoryDetailsAdpater);
                                    vendor_list_recyclerview.setHasFixedSize(true);
                                    categoryDetailsAdpater.notifyDataSetChanged();
                                    categoryDetailsAdpater.setOnItemClickListener(productListCategoryModel -> {
                                        // New 09/04/2019
                                        Intent intent = new Intent(AtHomeActivity.this, VendorDetailsActivityNew.class);
                                        intent.putExtra("Tag", tag);
                                        intent.putExtra("vname", productListCategoryModel.getProduct_name());
                                        intent.putExtra("location", productListCategoryModel.getVlocation());
                                        intent.putExtra("image", productListCategoryModel.getVimage());
                                        intent.putExtra("rating", productListCategoryModel.getRating());
                                        intent.putExtra("vid", productListCategoryModel.getId());
                                        intent.putExtra("cid", productListCategoryModel.getCid());
                                        intent.putExtra("closingTime", productListCategoryModel.getOffer_end_date());
                                        intent.putExtra("openingTime", productListCategoryModel.getOffer_start_date());
                                        intent.putExtra("image1", productListCategoryModel.getImage1());
                                        intent.putExtra("image2", productListCategoryModel.getImage2());
                                        intent.putExtra("image3", productListCategoryModel.getImage3());
                                        intent.putExtra("image4", productListCategoryModel.getImage4());
                                        intent.putExtra("image5", productListCategoryModel.getImage5());
                                        intent.putExtra("subcat", "");
                                        intent.putExtra("discount", productListCategoryModel.getOffer_price());
                                        intent.putExtra("commision", productListCategoryModel.getCurrent_price());
                                        startActivity(intent);
                                    });



                                } else {
                                    Toast.makeText(AtHomeActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                vendor_list_recyclerview.setVisibility(View.VISIBLE);
                                //rl_no_details.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(AtHomeActivity.this, productListCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                vendor_list_recyclerview.setVisibility(View.VISIBLE);
                                //rvProductList.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(AtHomeActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductListCategoryResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AtHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
