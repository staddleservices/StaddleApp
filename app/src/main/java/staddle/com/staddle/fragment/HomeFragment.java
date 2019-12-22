package staddle.com.staddle.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.SliderImagesResponse;
import staddle.com.staddle.ResponseClasses.VendorListResponse;
import staddle.com.staddle.activity.AtHomeActivity;
import staddle.com.staddle.activity.SecurityGuardActivity;
import staddle.com.staddle.activity.SubCategoryDetailsActivity;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.adapter.SliderImageAdapter;
import staddle.com.staddle.adapter.VendorListNewAdapter;
import staddle.com.staddle.bean.SliderImagesModule;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.CheckNetwork;

public class HomeFragment extends Fragment {

    Context mContext;
    ViewPager viewpager_home_slider;
    SliderImageAdapter sliderImageAdapter;
    private ArrayList<SliderImagesModule> imageUrl;
    TextView[] dots;
    int page_position = 0;
    LinearLayout ll_dots;

    public RecyclerView productlist_recyclerview;
    ApiInterface apiInterface;
    public static ArrayList<VendorListModel> vendorListModelArrayList;
    TextView txtNoMeassage;

    VendorListNewAdapter vendorListNewAdapter;
    NestedScrollView nested_scroll_view;

    //Faceboook Shimmer
    private ShimmerFrameLayout mShimmerViewContainer;


    public HomeFragment() {

    }

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        if (getActivity() instanceof HomeActivity) {
            mContext = getActivity();
        }

        find_All_IDs(rootView);

        vendorListModelArrayList = new ArrayList<>();
        imageUrl = new ArrayList<>();
        productlist_recyclerview.setNestedScrollingEnabled(false);

        init();

        addBottomDots(0);

        final Handler handler = new Handler();
        final Runnable update = () -> {
            if (page_position == imageUrl.size()) {
                page_position = 0;
            } else {
                page_position = page_position + 1;
            }
            viewpager_home_slider.setCurrentItem(page_position, true);
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 3000);

        LinearLayout linearLayout1 = rootView.findViewById(R.id.ll_beauty);
        LinearLayout linearLayout2 = rootView.findViewById(R.id.ll_spa);
        LinearLayout linearLayout3 = rootView.findViewById(R.id.ll_house);
        LinearLayout linearLayout4 = rootView.findViewById(R.id.ll_security);

        linearLayout1.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, AtHomeActivity.class);
            intent.putExtra("cid","1");
            startActivity(intent);
        });

        linearLayout2.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, AtHomeActivity.class);
            intent.putExtra("cid", "4");
            mContext.startActivity(intent);
        });

        linearLayout3.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, AtHomeActivity.class);
            intent.putExtra("cid", "2");
            intent.putExtra("Tag", "HOUSE");
            mContext.startActivity(intent);
        });

        linearLayout4.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, AtHomeActivity.class);
            intent.putExtra("cid", "3");
            intent.putExtra("Tag", "Security");
            startActivity(intent);
        });

        if (CheckNetwork.isNetworkAvailable(mContext)) {
            getHomeSliderImages();
            vendorList();
        } else {
            Alerts.showAlert(mContext);
        }

        return rootView;
    }

    private void find_All_IDs(View view) {
        nested_scroll_view  = view.findViewById(R.id.nested_scroll_view);
        viewpager_home_slider = view.findViewById(R.id.viewpager_home_slider);
        ll_dots = view.findViewById(R.id.ll_dots);
        txtNoMeassage = view.findViewById(R.id.txtNoMeassage);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        productlist_recyclerview = view.findViewById(R.id.productlist_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productlist_recyclerview.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        productlist_recyclerview.setHasFixedSize(true);
        productlist_recyclerview.setItemViewCacheSize(20);
        productlist_recyclerview.setNestedScrollingEnabled(false);

    }

    private void vendorList() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String currLat = AppPreferences.loadPreferences(mContext, "LATTITUDE");
        String currLng = AppPreferences.loadPreferences(mContext, "LONGITUDE");
        //Toast.makeText(mContext, currLat+currLng, Toast.LENGTH_LONG).show();
        Call<VendorListResponse> call = apiInterface.getVendorList("26.867805", "75.818736 ");

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
                                    vendorListNewAdapter.setHasStableIds(true);
                                    productlist_recyclerview.setAdapter(vendorListNewAdapter);
                                } else {
                                    //Toast.makeText(mContext, "" + vendorListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                productlist_recyclerview.setVisibility(View.VISIBLE);
                                txtNoMeassage.setVisibility(View.GONE);
                            } else {
                                txtNoMeassage.setVisibility(View.VISIBLE);
                                productlist_recyclerview.setVisibility(View.GONE);
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
            public void onFailure(Call<VendorListResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(mContext, "server error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getHomeSliderImages() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SliderImagesResponse> call = apiInterface.getImagesList();

        call.enqueue(new Callback<SliderImagesResponse>() {
            @Override
            public void onResponse(@NonNull Call<SliderImagesResponse> call, @NonNull Response<SliderImagesResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("homeslider_Response", response + "");
                    assert response.body() != null;

                    imageUrl = response.body().getInfo();
                    String status = response.body().getStatus();
                    if (status.equalsIgnoreCase("1")) {
                        sliderImageAdapter = new SliderImageAdapter(mContext, imageUrl);
                        viewpager_home_slider.setAdapter(sliderImageAdapter);
                    } else {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert response.body() != null;
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SliderImagesResponse> call, @NonNull Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        viewpager_home_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void addBottomDots(int currentPage) {

        dots = new TextView[imageUrl.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(mContext);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#FFFFFF"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#000000"));
    }



    private void hideProgressDialog() {

            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            nested_scroll_view.setVisibility(View.VISIBLE);

    }


}
