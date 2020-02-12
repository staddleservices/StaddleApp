package staddle.com.staddle.fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class HouseKeepingFragment extends Fragment {
    Context mContext;
    RecyclerView rvBeautySalon;
    ApiInterface apiInterface;
    ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
    SubCategoryAdapter adapter;

    public HouseKeepingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_keeping_fragment, container, false);
        find_All_IDs(view);
        mContext = getActivity();

        if (staddle.com.staddle.utils.CheckNetwork.isNetworkAvailable(mContext)) {
            categorySubCategoryListHouse();
        } else {
            Alerts.showAlert(mContext);
        }

        return view;
    }

    private void find_All_IDs(View view) {
        rvBeautySalon = view.findViewById(R.id.rvBeautySalon);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBeautySalon.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
    }

    private void categorySubCategoryListHouse() {
       /* final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();*/

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CategorySubCategoryListResponse> call = apiInterface.getCategorySubCategoryList();

        call.enqueue(new Callback<CategorySubCategoryListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategorySubCategoryListResponse> call, @NonNull Response<CategorySubCategoryListResponse> response) {
                //  progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        subCategoryList.clear();
                        CategorySubCategoryListResponse subCategoryListResponse = response.body();
                        if (subCategoryListResponse != null) {
                            String status = subCategoryListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                subCategoryList = subCategoryListResponse.getInfo().get(1).getSub_Category();
                                adapter = new SubCategoryAdapter(mContext, subCategoryList, "", "House", "");
                                rvBeautySalon.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CategorySubCategoryListResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
