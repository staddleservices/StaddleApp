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
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.CategorySubCategoryListResponse;
import staddle.com.staddle.adapter.SubCategoryAdapter;
import staddle.com.staddle.bean.SubCategoryModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;

public class SecurityGuardActivity extends AppCompatActivity {
    ImageView iv_back;
    RecyclerView rvBeautySalon;
    ApiInterface apiInterface;
    ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
    SubCategoryAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_guard);
        AppConstants.ChangeStatusBarColor(SecurityGuardActivity.this);

        find_All_IDs();

        if (staddle.com.staddle.utils.CheckNetwork.isNetworkAvailable(SecurityGuardActivity.this)) {
            categorySubCategoryListSecurity();
        } else {
            Alerts.showAlert(SecurityGuardActivity.this);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void find_All_IDs() {
        rvBeautySalon = findViewById(R.id.rvBeautySalon);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SecurityGuardActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBeautySalon.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        iv_back=findViewById(R.id.iv_back);

    }

    private void categorySubCategoryListSecurity() {
        final ProgressDialog progressDialog = new ProgressDialog(SecurityGuardActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CategorySubCategoryListResponse> call = apiInterface.getCategorySubCategoryList();

        call.enqueue(new Callback<CategorySubCategoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategorySubCategoryListResponse> call, @NonNull Response<CategorySubCategoryListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        subCategoryList.clear();
                        CategorySubCategoryListResponse subCategoryListResponse = response.body();
                        if (subCategoryListResponse != null) {
                            String status = subCategoryListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                subCategoryList = subCategoryListResponse.getInfo().get(2).getSub_Category();
                                adapter = new SubCategoryAdapter(SecurityGuardActivity.this, subCategoryList, "", "", "Security");
                                rvBeautySalon.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(SecurityGuardActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CategorySubCategoryListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SecurityGuardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
