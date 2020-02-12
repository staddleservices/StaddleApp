package staddle.com.staddle.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.VendorListResponse;
import staddle.com.staddle.adapter.VendorListNewAdapter;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.CheckNetwork;

public class SearchFragment extends Fragment {

    private Context mContext;
    private ProgressDialog progressDialog;
    private RecyclerView rv_vendorlist;
    private ArrayList<VendorListModel> vendorListModelArrayList;
    private VendorListNewAdapter vendorListNewAdapter;
    private SearchView searchViewHome;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        if (getActivity() instanceof HomeActivity) {
            mContext = getActivity();
        }

        setUpView(view);

        if (CheckNetwork.isNetworkAvailable(mContext)) {
            vendorList();
        } else {
            Alerts.showAlert(mContext);
        }

        searchViewHome.setIconified(true);
        searchViewHome.setFocusable(false);

        searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (vendorListNewAdapter != null) {
                    vendorListNewAdapter.getFilter().filter(s);
                } else {
                    Toast.makeText(mContext, "not", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (vendorListNewAdapter != null) {
                    vendorListNewAdapter.getFilter().filter(s);
                } else {
                    Toast.makeText(mContext, "Product not available here", Toast.LENGTH_LONG).show();
                    searchViewHome.setVisibility(View.GONE);
                }
                return false;
            }
        });

        return view;
    }

    private void setUpView(View view) {
        vendorListModelArrayList = new ArrayList<>();
        searchViewHome = view.findViewById(R.id.searchViewHomee);

        rv_vendorlist = view.findViewById(R.id.beautysalon_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rv_vendorlist.setLayoutManager(linearLayoutManager);
        rv_vendorlist.setHasFixedSize(true);
        vendorListNewAdapter = new VendorListNewAdapter(mContext, vendorListModelArrayList);
        rv_vendorlist.setAdapter(vendorListNewAdapter);
        vendorListNewAdapter.notifyDataSetChanged();
    }

    private void vendorList() {
        showProgressDialog();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String currLat = AppPreferences.loadPreferences(mContext, "LATTITUDE");
        String currLng = AppPreferences.loadPreferences(mContext, "LONGITUDE");

        Call<VendorListResponse> call = apiInterface.getVendorList(currLat, currLng);
        call.enqueue(new Callback<VendorListResponse>() {
            @Override
            public void onResponse(@NonNull Call<VendorListResponse> call, @NonNull Response<VendorListResponse> response) {
                try {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        vendorListModelArrayList.clear();
                        VendorListResponse vendorListResponse = response.body();
                        if (vendorListResponse != null) {
                            String status = vendorListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                vendorListModelArrayList = vendorListResponse.getInfo();

                                if (vendorListModelArrayList != null) {
                                    vendorListNewAdapter = new VendorListNewAdapter(mContext, vendorListModelArrayList);
                                    rv_vendorlist.setAdapter(vendorListNewAdapter);
                                    vendorListNewAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, "null ", Toast.LENGTH_SHORT).show();
                                }
                                rv_vendorlist.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(mContext, vendorListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rv_vendorlist.setVisibility(View.GONE);
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
            public void onFailure(@NonNull Call<VendorListResponse> call, @NonNull Throwable t) {
                hideProgressDialog();
                Toast.makeText(mContext, "server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}

