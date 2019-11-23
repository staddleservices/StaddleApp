package staddle.com.staddle.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class FavoriteFragment extends Fragment {
    View view;
    Context mContext;
    RecyclerView rvFavourite;
    TextView yourfavstores;
    ApiInterface apiInterface;
    ArrayList<FavouriteListModel> favouriteListModelslist;
    FavouriteListAdapter favouriteListAdapter;
    String userId;
    RelativeLayout rl_no_fav;
    TextView txt_no_fav;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mContext = getActivity();
        favouriteListModelslist = new ArrayList<>();
        userId = AppPreferences.loadPreferences(mContext, "USER_ID");

        find_All_IDs(view);

        if (staddle.com.staddle.utils.CheckNetwork.isNetworkAvailable(mContext)) {
            getFavouriteList(userId);
        } else {
            Alerts.showAlert(mContext);
        }

        return view;
    }

    private void find_All_IDs(View view) {
        rvFavourite = view.findViewById(R.id.rvFavourite);
        txt_no_fav = view.findViewById(R.id.txt_no);
        rl_no_fav = view.findViewById(R.id.rl_no);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFavourite.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        yourfavstores=view.findViewById(R.id.yourfavtext);
    }

    private void getFavouriteList(String uid) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Please Wait..."); // set message
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
                                favouriteListAdapter = new FavouriteListAdapter(mContext, favouriteListModelslist);
                                rvFavourite.setAdapter(favouriteListAdapter);
                                rvFavourite.setVisibility(View.VISIBLE);
                                yourfavstores.setVisibility(View.VISIBLE);
                                rl_no_fav.setVisibility(View.GONE);
                                rvFavourite.setHasFixedSize(true);
                                favouriteListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                rl_no_fav.setVisibility(View.VISIBLE);
                                rvFavourite.setVisibility(View.GONE);
                                yourfavstores.setVisibility(View.GONE);
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
            public void onFailure(Call<FavouriteListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
