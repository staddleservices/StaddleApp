package staddle.com.staddle.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.AddFavouriteResponse;
import staddle.com.staddle.ResponseClasses.CheckFavoriteResponse;
import staddle.com.staddle.ResponseClasses.GetVendorSubCategoryMenuListResponse;
import staddle.com.staddle.adapter.VendorSubCategoryListAdapter;
import staddle.com.staddle.bean.GetVendorSubCategoryListModule;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.bean.VendorOfferListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.retrofitApi.BaseApi;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;
import staddle.com.staddle.utils.CheckNetwork;

public class VendorDetailsActivityNew extends AppCompatActivity {

    private TextView txtVendorName, txtRating,
            txtUserEmail, txtClosingTime, txtOpeningTime, txtVendorLocation, txtCart;
    ImageView iv_favourite1, iv_favourite;
    private RecyclerView recyclerView;
    private AppBarLayout app_bar;
    private VendorSubCategoryListAdapter vendorSubCategoryListAdapter;
    private ArrayList<GetVendorSubCategoryMenuListModule> categoryMenuList;

    private ApiInterface apiInterface;
    private ImageView img_back, iv_vendor, img1, img2, img3, img4, img5, img_vendor_logo ;
    public static  RelativeLayout img_cart;
    private String vid = "", rating = "", cid = "",  location = "", image = "", closingTime = "", openingTime = "",
            image1 = "", image2 = "", image3 = "", image4 = "", image5 = "", userId, discount = "", commision = "";
    public static String vname = "";
    //private ru.dimorinny.floatingtextbutton.FloatingTextButton floatingTextButton;
    private FloatingActionButton floatingTextButton;
    private ProgressDialog progressDialog;
    private int lastPosition = -1;
    private Dialog dialog;
    private String fav;
    private String tag = "", Category = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details_new);
        AppConstants.ChangeStatusBarColor(VendorDetailsActivityNew.this);

        find_All_IDs();
        userId = AppPreferences.loadPreferences(VendorDetailsActivityNew.this, "USER_ID");

        categoryMenuList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            tag = intent.getStringExtra("Tag");
            Category = intent.getStringExtra("Category");
            vname = intent.getStringExtra("vname");
            location = intent.getStringExtra("location");
            rating = intent.getStringExtra("rating");
            image = intent.getStringExtra("image");
            vid = intent.getStringExtra("vid");
            //Log.e("VIDDETAILS:",vid);
            cid = intent.getStringExtra("cid");
            discount = intent.getStringExtra("discount");
            commision = intent.getStringExtra("commision");
            AppPreferences.savePreferences(VendorDetailsActivityNew.this, "cidAtVendorDetails", cid);
            openingTime = intent.getStringExtra("openingTime");
            closingTime = intent.getStringExtra("closingTime");

            image1 = intent.getStringExtra("image1");
            image2 = intent.getStringExtra("image2");
            image3 = intent.getStringExtra("image3");
            image4 = intent.getStringExtra("image4");
            image5 = intent.getStringExtra("image5");

        }

        chekFavorite();

        txtVendorName.setText(vname + " ");
        txtUserEmail.setText(vname);
        txtRating.setText(rating);
        txtOpeningTime.setText(openingTime);
        txtClosingTime.setText(closingTime);
        txtVendorLocation.setText(location);

        String image_url = BaseApi.IMAGE_BASE_POST_URL + image;
        String image1_url = BaseApi.IMAGE_BASE_POST_URL + image1;
        String image2_url = BaseApi.IMAGE_BASE_POST_URL + image2;
        String image3_url = BaseApi.IMAGE_BASE_POST_URL + image3;
        String image4_url = BaseApi.IMAGE_BASE_POST_URL + image4;
        String image5_url = BaseApi.IMAGE_BASE_POST_URL + image5;
        String img_vendor_logo_url = BaseApi.IMAGE_BASE_POST_URL + image;

        Picasso.get()
                .load(image_url)
                .into(iv_vendor);

        Picasso.get()
                .load(img_vendor_logo_url)
                .into(img_vendor_logo);

        Picasso.get()
                .load(image1_url)
                .into(img1);

        Picasso.get()
                .load(image2_url)
                .into(img2);

        Picasso.get()
                .load(image3_url)
                .into(img3);

        Picasso.get()
                .load(image4_url)
                .into(img4);

        Picasso.get()
                .load(image5_url)
                .into(img5);

        img_cart.setOnClickListener(view -> {
            VendorSubCategoryListAdapter.getCartList();
            Intent intent1 = new Intent(VendorDetailsActivityNew.this, HomeActivity.class);
            intent1.putExtra("openF2", true);
            if (discount.equalsIgnoreCase(""))
                intent1.putExtra("discount", "0%");
            else if (!discount.contains("%"))
                intent1.putExtra("discount", discount + "%");
            else
                intent1.putExtra("discount", discount);

            if (commision.equalsIgnoreCase(""))
                intent1.putExtra("commision", "0%");
            else if (!commision.contains("%"))
                intent1.putExtra("commision", commision + "%");
            else
                intent1.putExtra("commision", commision);

            intent1.putExtra("Tag", tag);
            intent1.putExtra("Category", Category);
            intent1.putExtra("CID", cid);
            intent1.putExtra("vid",vid);
            intent.putExtra("vname",vname);

            overridePendingTransition(0, 0);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent1);
//            finish();
        });

        //on click event of images
        img_vendor_logo.setOnClickListener(view -> Picasso.get()
                .load(image_url)
                .into(iv_vendor));

        img1.setOnClickListener(view -> Picasso.get()
                .load(image1_url)
                .into(iv_vendor));

        img2.setOnClickListener(view -> Picasso.get()
                .load(image2_url)
                .into(iv_vendor));

        img3.setOnClickListener(view -> Picasso.get()
                .load(image3_url)
                .into(iv_vendor));

        img4.setOnClickListener(view -> Picasso.get()
                .load(image4_url)
                .into(iv_vendor));

        img5.setOnClickListener(view -> Picasso.get()
                .load(image5_url)
                .into(iv_vendor));

        setUpView();

        if (CheckNetwork.isNetworkAvailable(this)) {
            getVendorSubCategoryMenuListApi(vid);
        } else {
            Alerts.showAlert(this);
        }

        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption();
                } else if (isShow) {
                    isShow = false;
                    hideOption();
                }
            }
        });

        img_back.setOnClickListener(view -> onBackPressed());

        floatingTextButton.setOnClickListener(view -> showPopup());

        iv_favourite.setOnClickListener(view -> {
            if (CheckNetwork.isNetworkAvailable(getApplicationContext())) {
                addFavourite(userId, vid);
            } else {
                Alerts.showAlert(getApplicationContext());
            }
        });

        iv_favourite1.setOnClickListener(view -> {
            if (CheckNetwork.isNetworkAvailable(getApplicationContext())) {
                addFavourite(userId, vid);
            } else {
                Alerts.showAlert(getApplicationContext());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpView();
    }

    private void setUpView() {
        vendorSubCategoryListAdapter = new VendorSubCategoryListAdapter(VendorDetailsActivityNew.this, categoryMenuList, "", vname);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(vendorSubCategoryListAdapter);
        vendorSubCategoryListAdapter.notifyDataSetChanged();

        vendorSubCategoryListAdapter.setOnItemClickListener((position, menuListt) -> {

        });

        updateCart(VendorSubCategoryListAdapter.getCartList());

    }

    private void find_All_IDs() {
        app_bar = findViewById(R.id.app_bar);

        txtVendorName = findViewById(R.id.txtVendorName);
        txtRating = findViewById(R.id.txtRating);
        iv_vendor = findViewById(R.id.iv_vendor);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        img_back = findViewById(R.id.img_back);
        txtOpeningTime = findViewById(R.id.txtOpeningTime);
        txtClosingTime = findViewById(R.id.txtClosingTime);
        txtVendorLocation = findViewById(R.id.txtVendorLocation);
        txtCart = findViewById(R.id.txtCart);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img_vendor_logo = findViewById(R.id.img_vendor_logo);
        img_cart = findViewById(R.id.img_cart);
        iv_favourite = findViewById(R.id.iv_favourite);
        iv_favourite1 = findViewById(R.id.iv_favourite1);

        floatingTextButton = findViewById(R.id.fab_action_button);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorDetailsActivityNew.this));
    }

    private void hideOption() {
        txtUserEmail.setVisibility(View.GONE);
    }

    private void showOption() {
        txtUserEmail.setVisibility(View.VISIBLE);
    }

    private void getVendorSubCategoryMenuListApi(String vid) {
        showProgressDialog();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<GetVendorSubCategoryMenuListResponse> call = apiInterface.getVenderSubCatgoryMenuList(vid);

        call.enqueue(new Callback<GetVendorSubCategoryMenuListResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetVendorSubCategoryMenuListResponse> call, Response<GetVendorSubCategoryMenuListResponse> response) {
                hideProgressDialog();
                try {
                    String serverCode = String.valueOf(response.code());
                    if (response.isSuccessful()) {
                        GetVendorSubCategoryMenuListResponse categoryListResponse = response.body();
                        if (categoryListResponse != null) {
                            String status = categoryListResponse.getStatus();
                            String message = categoryListResponse.getMessage();
                            if (status.equalsIgnoreCase("1")) {
                                categoryMenuList = categoryListResponse.getInfo();
                                recyclerView.setVisibility(View.VISIBLE);
                                setUpView();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(VendorDetailsActivityNew.this, "Response Getting Null !!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(VendorDetailsActivityNew.this, serverCode + "Server Error !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetVendorSubCategoryMenuListResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(VendorDetailsActivityNew.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("RtlHardcoded")
    private void showPopup() {
        dialog = new Dialog(VendorDetailsActivityNew.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_window);
        dialog.show();

        RecyclerView rvPopUp = dialog.findViewById(R.id.rvPopUp);
        rvPopUp.setLayoutManager(new LinearLayoutManager(VendorDetailsActivityNew.this));

        vendorSubCategoryListAdapter = new VendorSubCategoryListAdapter(VendorDetailsActivityNew.this, categoryMenuList, "isServiceType", vname);
        rvPopUp.setAdapter(vendorSubCategoryListAdapter);

        vendorSubCategoryListAdapter.setOnItemClickListener((position, menuListt) -> {
            String nameSubCatPopup = menuListt.getVender_sub_catgoey();
            for (int i = 0; i < categoryMenuList.size(); i++) {
                if (categoryMenuList.get(i).getVender_sub_catgoey().equals(nameSubCatPopup)) {
                    scroolView(i);
                    break;
                }
            }
            dialog.dismiss();
        });
    }

    private void scroolView(int i) {
        app_bar.setExpanded(false);
        if (i == 0) {
            app_bar.setExpanded(true);
            recyclerView.smoothScrollToPosition(0);
        } else if (i == categoryMenuList.size() - 1)
            recyclerView.smoothScrollToPosition(categoryMenuList.size() - 1);
        else
            recyclerView.smoothScrollToPosition(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(VendorDetailsActivityNew.this,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(VendorDetailsActivityNew.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void addFavourite(String userId, String vid) {
        showProgressDialog();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AddFavouriteResponse> call = apiInterface.addFavourite(userId, vid);

        call.enqueue(new Callback<AddFavouriteResponse>() {
            @Override
            public void onResponse(Call<AddFavouriteResponse> call, final Response<AddFavouriteResponse> response) {
                hideProgressDialog();
                try {
                    if (response.isSuccessful()) {
                        AddFavouriteResponse responsee = response.body();
                        if (responsee != null) {
                            String message = responsee.getMessage();
                            int status = responsee.getStatus();
                            if (status == 0) {
                                iv_favourite.setVisibility(View.VISIBLE);
                                iv_favourite1.setVisibility(View.GONE);
                                Toast.makeText(VendorDetailsActivityNew.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else if (status == 1) {
                                iv_favourite.setVisibility(View.GONE);
                                iv_favourite1.setVisibility(View.VISIBLE);
                                Toast.makeText(VendorDetailsActivityNew.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(VendorDetailsActivityNew.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddFavouriteResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(VendorDetailsActivityNew.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void chekFavorite() {
        // showProgressDialog();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CheckFavoriteResponse> call = apiInterface.checkFav(userId, vid);

        call.enqueue(new Callback<CheckFavoriteResponse>() {
            @Override
            public void onResponse(Call<CheckFavoriteResponse> call, Response<CheckFavoriteResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        Integer status = response.body().getStatus();
                        String message = response.body().getMessage();
                        if (status == 1) {
                            ArrayList<CheckFavoriteResponse.Info> info = response.body().getInfo();
                            for (CheckFavoriteResponse.Info info1 : info) {
                                fav = info1.getFavStatus();
                            }
                            if (fav.equalsIgnoreCase("Y")) {
                                iv_favourite.setVisibility(View.GONE);
                                iv_favourite1.setVisibility(View.VISIBLE);
                            } else if (fav.equalsIgnoreCase("N")) {
                                iv_favourite.setVisibility(View.VISIBLE);
                                iv_favourite1.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(VendorDetailsActivityNew.this, "Server Error !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CheckFavoriteResponse> call, Throwable t) {
                Toast.makeText(VendorDetailsActivityNew.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateCart(int items) {
        if(items==0){
            img_cart.setVisibility(View.GONE);
            txtCart.setText("" + items+" Item");
        }else{
            img_cart.setVisibility(View.VISIBLE);
            txtCart.setText("" + items+" Item");
        }

    }
}