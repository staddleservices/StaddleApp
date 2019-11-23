package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.ProductListCategoryResponse;
import staddle.com.staddle.ResponseClasses.SubcategoryTreeListResponse;
import staddle.com.staddle.adapter.CategoryDetailsAdpater;
import staddle.com.staddle.adapter.SubCategoryDetailsAdapter;
import staddle.com.staddle.bean.FilterSubCategoryProductListModel;
import staddle.com.staddle.bean.ProductListCategoryModel;
import staddle.com.staddle.bean.SubcategoryTreeListModel;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;
import staddle.com.staddle.utils.CheckNetwork;

public class SubCategoryDetailsActivity extends AppCompatActivity {
    RecyclerView rvProductList, category_list_recyclerview;
    ImageView ivFilter, iv_back, iv_back_new, ivClearText, iv_search_new;
    RelativeLayout rl_no_details;
    TextView txtAll;
    ApiInterface apiInterface;
    String cid = "";
    ProgressDialog pd;
    SearchView searchView;
    SubCategoryDetailsAdapter subCategoryDetailsAdapter;
    ArrayList<SubcategoryTreeListModel> subcategoryTreeListModelArrayList;
    ArrayList<FilterSubCategoryProductListModel> filterSubCategoryProductListModelArrayList;
    ArrayList<ProductListCategoryModel> productListCategoryModelArrayListt;
    CategoryDetailsAdpater categoryDetailsAdpater;
    ArrayList<VendorListModel> vendorListModelArrayList;
    EditText edtSearchText;
    private String isMenWomen = "", subCat = "", tag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_details);
        AppConstants.ChangeStatusBarColor(SubCategoryDetailsActivity.this);

        find_All_IDs();

        Intent intent = getIntent();
        if (intent != null) {
            cid = intent.getStringExtra("cid");
            isMenWomen = intent.getStringExtra("isMenWomen");
            subCat = intent.getStringExtra("SubCat");
            tag = intent.getStringExtra("Tag");
        }

        productListCategoryModelArrayListt = new ArrayList<>();
        subcategoryTreeListModelArrayList = new ArrayList<>();
        filterSubCategoryProductListModelArrayList = new ArrayList<>();
        vendorListModelArrayList = new ArrayList<>();

        if (CheckNetwork.isNetworkAvailable(this)) {
            subCategoryTreeDetailsListt(cid);
        } else {
            Alerts.showAlert(this);
        }

        txtAll.setOnClickListener(view -> {
            productListcategory(cid, "");
        });

        iv_back_new.setOnClickListener(view -> onBackPressed());

        searchView.setIconified(true);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (categoryDetailsAdpater != null) {
                    filter(s);
                } else {
                    Toast.makeText(SubCategoryDetailsActivity.this, "Product not available here", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // filter recycler view when text is changed
                if (categoryDetailsAdpater != null) {
                    filter(s);
                } else {
                    Toast.makeText(SubCategoryDetailsActivity.this, "Product not available here", Toast.LENGTH_LONG).show();
                    searchView.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void find_All_IDs() {
        rvProductList = findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(SubCategoryDetailsActivity.this));

        ivFilter = findViewById(R.id.ivFilter);
        iv_back = findViewById(R.id.iv_back);
        iv_back_new = findViewById(R.id.iv_back_new);
        rl_no_details = findViewById(R.id.rl_no_details);

        edtSearchText = findViewById(R.id.edtSearchText);
        ivClearText = findViewById(R.id.ivClearText);
        iv_search_new = findViewById(R.id.iv_search_new);

        searchView = findViewById(R.id.searchView);
        txtAll = findViewById(R.id.txtAll);

        category_list_recyclerview = findViewById(R.id.category_list_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubCategoryDetailsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        category_list_recyclerview.setLayoutManager(linearLayoutManager);

    }

    private void subCategoryTreeDetailsListt(String cid) {
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
                                    subCategoryDetailsAdapter = new SubCategoryDetailsAdapter(SubCategoryDetailsActivity.this, subcategoryTreeListModelArrayList);
                                    category_list_recyclerview.setAdapter(subCategoryDetailsAdapter);
                                    category_list_recyclerview.setVisibility(View.VISIBLE);
                                    rl_no_details.setVisibility(View.GONE);
                                    subCategoryDetailsAdapter.notifyDataSetChanged();

                                    subCategoryDetailsAdapter.setOnItemClickListener(subcategoryTreeListModel -> {
                                        productListcategory(subcategoryTreeListModel.getCid(), subcategoryTreeListModel.getSub_name());
                                    });
                                } else {
                                    rl_no_details.setVisibility(View.VISIBLE);
                                    category_list_recyclerview.setVisibility(View.GONE);
                                }
                            }
                            productListcategory(cid, "");
                        }
                    } else {
                        Toast.makeText(SubCategoryDetailsActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubcategoryTreeListResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(SubCategoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

                                    categoryDetailsAdpater = new CategoryDetailsAdpater(SubCategoryDetailsActivity.this, productListCategoryModelArrayListt);
                                    rvProductList.setAdapter(categoryDetailsAdpater);
                                    rvProductList.setHasFixedSize(true);
                                    categoryDetailsAdpater.notifyDataSetChanged();

                                    categoryDetailsAdpater.setOnItemClickListener(productListCategoryModel -> {
                                        // New 09/04/2019
                                        Intent intent = new Intent(SubCategoryDetailsActivity.this, VendorDetailsActivityNew.class);
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
                                    Toast.makeText(SubCategoryDetailsActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvProductList.setVisibility(View.VISIBLE);
                                rl_no_details.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(SubCategoryDetailsActivity.this, productListCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rl_no_details.setVisibility(View.VISIBLE);
                                rvProductList.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(SubCategoryDetailsActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductListCategoryResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SubCategoryDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void filter(String text) {
        ArrayList<ProductListCategoryModel> temp = new ArrayList<>();
        for (ProductListCategoryModel d : productListCategoryModelArrayListt) {
            if ((d.getProduct_name().toLowerCase()).contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        categoryDetailsAdpater.updateList(temp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}


