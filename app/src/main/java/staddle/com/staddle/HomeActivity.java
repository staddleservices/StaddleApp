package staddle.com.staddle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.ResponseClasses.UserInfoResponse;
import staddle.com.staddle.activity.AboutUsActivity;
import staddle.com.staddle.activity.HelpActivity;
import staddle.com.staddle.activity.LocationActivity;
import staddle.com.staddle.activity.MyOrderActivity;
import staddle.com.staddle.activity.MyWishListActivity;
import staddle.com.staddle.activity.NotificationActivity;
import staddle.com.staddle.activity.OfferAndPromoActivity;
import staddle.com.staddle.activity.PolicyActivity;
import staddle.com.staddle.activity.ProfileActivity;
import staddle.com.staddle.activity.SettingActivity;
import staddle.com.staddle.activity.WalletActivity;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.bean.UserInfoModule;
import staddle.com.staddle.fcm.DBManager;
import staddle.com.staddle.fragment.BeautyFragment;
import staddle.com.staddle.fragment.FavoriteFragment;
import staddle.com.staddle.fragment.HomeFragment;
import staddle.com.staddle.fragment.HouseKeepingFragment;
import staddle.com.staddle.fragment.ProfileFragment;
import staddle.com.staddle.fragment.SearchFragment;
import staddle.com.staddle.fragment.SecurityFragment;
import staddle.com.staddle.fragment.ShoppingFragment;
import staddle.com.staddle.fragment.WalletFragment;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.internetconnection.NetworkAvailablity;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

import static android.graphics.Color.TRANSPARENT;
import static staddle.com.staddle.fcm.MyFireBaseMessagingService.CURRENT_ORDER_ID_KEY;
import static staddle.com.staddle.fcm.MyFireBaseMessagingService.CURRENT_ORDER_STATUS_KEY;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    ApiInterface apiInterface;
    String TAG = getClass().getSimpleName();
    String userId, userName, userEmail, userMobile, userProfilePicture;
    ImageView iv_menu_toolbar, iv_user_profile_pic;
    TextView tv_user_name, tv_user_email;
    DrawerLayout drawer;
    TextView et_location;
    RelativeLayout rl_edt_profile,relSearch;
    public static String discount = "";
    public static String tag = "";
    public static String commision = "";
    public static String vname = "";
    public static String vid="";
    public static String ttldiscount = "";
    private ImageView iv_bell;

    public static ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myCartArrayList = new ArrayList<>();

    RelativeLayout ll_nav_home, ll_nav_rate_and_review, ll_nav_my_profile, ll_nav_my_vendor_list, ll_nav_my_order, rl_nav_my_wish_list,
            ll_nav_promo_or_offer, ll_nav_wallet, ll_nav_notification, ll_nav_invite_friends,
            ll_nav_settings, ll_nav_24_x_7_help, rl_nav_policy, ll_nav_about, ll_nav_logout, rl_notification;

    FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView navigation;
    Context mContext;
    List<Address> list;
    String currLat, currLong, address;
    String cid="";
    String vaname="";

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    public static  FirebaseAuth mAuth;
    public static GoogleSignInClient mGoogleSignInClient;
    boolean openF2;
    Fragment fragmentShopping = null;
    public static RelativeLayout toolbar;
    private String Category = "";


    //SearchBox
    TextView searchBox;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppConstants.ChangeStatusBarColor(HomeActivity.this);

        find_All_IDs();

//        Intent intent1=getIntent();
//        userId=intent1.getStringExtra("USER_ID");
//        userName=intent1.getStringExtra("USER_NAME");
//        userEmail=intent1.getStringExtra("USER_EMAIL");
//        userProfilePicture=intent1.getStringExtra("USER_PROFILE_PIC");
//        et_location.setText(intent1.getStringExtra("LOCATION"));
//        cid=intent1.getStringExtra("CID");
        userId = AppPreferences.loadPreferences(HomeActivity.this, "USER_ID");
        userName = AppPreferences.loadPreferences(HomeActivity.this, "USER_NAME");
        userEmail = AppPreferences.loadPreferences(HomeActivity.this, "USER_EMAIL");
        userProfilePicture = AppPreferences.loadPreferences(HomeActivity.this, "USER_PROFILE_PIC");
        et_location.setText(AppPreferences.loadPreferences(HomeActivity.this, "LOCATION"));

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("openF2")) {
            openF2 = extras.getBoolean("openF2");
            tag = extras.getString("Tag");
            Category = extras.getString("Category");
            discount = extras.getString("discount");
            commision = extras.getString("commision");
            vid=extras.getString("vid");
            cid=extras.getString("CID");
            vaname=extras.getString("vname");
        }
        if (openF2) {
            Bundle bundle = new Bundle();
            bundle.putString("Tag", tag);
            bundle.putString("Category", Category);
            bundle.putString("CID", cid);
            bundle.putString("vid",vid);
            bundle.putString("vname",vaname);
            fragmentShopping = ShoppingFragment.newInstance(bundle);
            navigation.setSelectedItemId(R.id.navigation_shopping);
            replaceFragment(fragmentShopping);
        }

        if (CheckNetwork.isNetworkAvailable(HomeActivity.this)) {
            getUserInfo(userId);
        } else {
            Toast.makeText(HomeActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

//        tv_user_name.setText(userName);
//        tv_user_email.setText(userEmail);

//        rl_edt_profile.setOnClickListener(v -> {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        });


        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment=new SearchFragment();
                toolbar.setVisibility(View.GONE);
                replaceFragment(searchFragment);
            }
        });

        navigation.setOnNavigationItemSelectedListener(this);

        if (!openF2) {
            navigation.setSelectedItemId(R.id.navigation_home);
        }

        et_location.setOnClickListener(view -> ChangeLocation(1));
        relSearch.setOnClickListener(view -> ChangeLocation(1));
//        ll_nav_home.setOnClickListener(this);
//        iv_menu_toolbar.setOnClickListener(this);
//        ll_nav_my_profile.setOnClickListener(this);
//        ll_nav_my_order.setOnClickListener(this);
//        rl_nav_my_wish_list.setOnClickListener(this);
//        ll_nav_promo_or_offer.setOnClickListener(this);
//        ll_nav_wallet.setOnClickListener(this);
//        ll_nav_notification.setOnClickListener(this);
//        ll_nav_invite_friends.setOnClickListener(this);
//        ll_nav_rate_and_review.setOnClickListener(this);
//        ll_nav_settings.setOnClickListener(this);
//        ll_nav_24_x_7_help.setOnClickListener(this);
//        rl_nav_policy.setOnClickListener(this);
//        ll_nav_about.setOnClickListener(this);
//        ll_nav_logout.setOnClickListener(this);

        iv_bell.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
        ;

        if(AppPreferences.loadPreferences(HomeActivity.this,CURRENT_ORDER_ID_KEY)!= null){
            String status=AppPreferences.loadPreferences(HomeActivity.this,CURRENT_ORDER_STATUS_KEY);
            if(status.equals("C")){
                //Show Rating box
                Toast.makeText(mContext, "Show rating box", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(HomeActivity.this)) {
            getUserInfo(userId);
        } else {
            Toast.makeText(HomeActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

    }


    private void find_All_IDs() {
        drawer = findViewById(R.id.drawer_layout);
        iv_menu_toolbar = findViewById(R.id.iv_menu_toolbar);
        et_location = findViewById(R.id.et_location);
        //rl_notification = findViewById(R.id.rl_notification);


        // ==================== Navigation View ============================
        relSearch = findViewById(R.id.relSearch);
        iv_bell = findViewById(R.id.iv_bell);
        rl_edt_profile = findViewById(R.id.rl_edt_profile);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_email = findViewById(R.id.tv_user_email);
        iv_user_profile_pic = findViewById(R.id.iv_user_profile_pic);

        //===================== Navigation View ============================
        ll_nav_home = findViewById(R.id.ll_nav_home);
        ll_nav_my_profile = findViewById(R.id.ll_nav_my_profile);
        ll_nav_my_order = findViewById(R.id.ll_nav_my_order);

        rl_nav_my_wish_list = findViewById(R.id.rl_nav_my_wish_list);
        ll_nav_promo_or_offer = findViewById(R.id.ll_nav_promo_or_offer);
        ll_nav_wallet = findViewById(R.id.ll_nav_wallet);
        ll_nav_notification = findViewById(R.id.ll_nav_notification);
        ll_nav_invite_friends = findViewById(R.id.ll_nav_invite_friends);
        ll_nav_rate_and_review = findViewById(R.id.ll_nav_rate_and_review);
        ll_nav_settings = findViewById(R.id.ll_nav_settings);
        ll_nav_24_x_7_help = findViewById(R.id.ll_nav_24_x_7_help);
        rl_nav_policy = findViewById(R.id.rl_nav_policy);
        ll_nav_about = findViewById(R.id.ll_nav_about);
        ll_nav_logout = findViewById(R.id.ll_nav_logout);

        navigation = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        searchBox=findViewById(R.id.searchservicesbox);

        Toast.makeText(HomeActivity.this, "+"+AppPreferences.loadPreferences(HomeActivity.this,CURRENT_ORDER_ID_KEY), Toast.LENGTH_SHORT).show();
        replaceFragment(new HomeFragment());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            String latitude = String.valueOf(place.getLatLng().latitude);
            String longitude = String.valueOf(place.getLatLng().longitude);
            AppPreferences.savePreferences(HomeActivity.this, "LATTITUDE", latitude);
            AppPreferences.savePreferences(HomeActivity.this, "LONGITUDE", longitude);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                list = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                if (list != null && list.size() > 0) {
                    String address = list.get(0).getAddressLine(0);
                    String city = list.get(0).getLocality();
                    et_location.setText(address);
                    AppPreferences.savePreferences(HomeActivity.this, "LOCATION", address);
                    Bundle bundle = new Bundle();
                    bundle.putString("CURRLAT", latitude);
                    bundle.putString("CURRLAN", longitude);
                    replaceFragment(HomeFragment.newInstance(bundle));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void ChangeLocation(int requestCode) {
        if (NetworkAvailablity.chkStatus(HomeActivity.this)) {
            try {
                mContext = HomeActivity.this;
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build((Activity) mContext);
                startActivityForResult(intent, requestCode);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(HomeActivity.this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if (id == R.id.ll_nav_home) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (id == R.id.ll_nav_my_profile) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_my_order) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.rl_nav_my_wish_list) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, MyWishListActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_promo_or_offer) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, OfferAndPromoActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_wallet) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, WalletActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_notification) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_invite_friends) {
//            drawer.closeDrawer(GravityCompat.START);
//            share();
//        } else if (id == R.id.ll_nav_settings) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_24_x_7_help) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_rate_and_review) {
//            drawer.closeDrawer(GravityCompat.START);
//            openAppRatingg(HomeActivity.this);
//        } else if (id == R.id.rl_nav_policy) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, PolicyActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_about) {
//            drawer.closeDrawer(GravityCompat.START);
//            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        } else if (id == R.id.ll_nav_logout) {
//            drawer.closeDrawer(GravityCompat.START);
//            logOut();
//        } else if (id == R.id.iv_menu_toolbar) {
//            drawer.openDrawer(GravityCompat.START);
//        }
//    }

    public static void openAppRatingg(Context context) {
        try {
            Uri marketUri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            context.startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            context.startActivity(marketIntent);
        }
    }

    public void share() {
        //To automatically fill in the application name and application id you could use this:
        int applicationNameId = this.getApplicationInfo().labelRes;
        final String appPackageName = this.getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, this.getString(applicationNameId));
        String text = "Install this cool application: ";
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
        startActivity(Intent.createChooser(i, "Share link:"));
    }

    private void getUserInfo(String userId) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<UserInfoResponse> call = apiInterface.getUserInfo(userId);

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        UserInfoResponse userInfoResponse = response.body();

                        if (userInfoResponse != null) {
                            ArrayList<UserInfoModule> loginInfoModelArrayList = userInfoResponse.getInfo();

                            String status = userInfoResponse.getStatus();
                            String message = userInfoResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {

                                for (UserInfoModule userInfoModule : loginInfoModelArrayList) {
                                    userName = userInfoModule.getUsername();
                                    userEmail = userInfoModule.getEmail();
                                    userMobile = userInfoModule.getMobile();
                                    userProfilePicture = userInfoModule.getImage();
                                }
                                tv_user_name.setText(userName);
                                tv_user_email.setText(userEmail);

                                if (userProfilePicture.equalsIgnoreCase("null")) {
                                    Picasso.get().load(R.drawable.user_profile).error(R.drawable.user_profile).into(iv_user_profile_pic);
                                } else if (userProfilePicture.contains(".png")) {
                                    Picasso.get()
                                            .load("http://127.0.0.1/mobileapp/post_image/"
                                                    + userProfilePicture)
                                            .error(R.drawable.user_profile)
                                            .into(iv_user_profile_pic);
                                } else if (!userProfilePicture.equalsIgnoreCase("")) {
                                    Picasso.get().load(userProfilePicture).error(R.drawable.user_profile).into(iv_user_profile_pic);
                                } else {
                                    Picasso.get().load(R.drawable.user_profile).error(R.drawable.user_profile).into(iv_user_profile_pic);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            for (Fragment fragment : fragmentManager.getFragments()) {
                String fragmentName = fragment.getClass().getSimpleName();
                if (fragmentName.equals(HomeFragment.class.getSimpleName())) {
                    if (doubleBackToExitPressedOnce) {
                        finishAffinity();
                        return;
                    }
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click back again to exit !!", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 1000);
                } else if (fragmentName.equals(BeautyFragment.class.getSimpleName())
                        || fragmentName.equals(FavoriteFragment.class.getSimpleName())
                        || fragmentName.equals(HouseKeepingFragment.class.getSimpleName())
                        || fragmentName.equals(SearchFragment.class.getSimpleName())
                        || fragmentName.equals(SecurityFragment.class.getSimpleName())
                        || fragmentName.equals(WalletFragment.class.getSimpleName())) {
                    navigation.setSelectedItemId(R.id.navigation_home);
                    replaceFragment(new HomeFragment());
                } else if (openF2) {
                    finish();
                } else {
                    navigation.setSelectedItemId(R.id.navigation_home);
                    replaceFragment(new HomeFragment());
                }
            }
        }
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        try {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    toolbar.setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    toolbar.setVisibility(View.GONE);
                    break;
                case R.id.navigation_wallet:
                    fragment = new WalletFragment();
                    toolbar.setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_shopping:
                    Bundle bundle = new Bundle();
                    bundle.putString("Tag", tag);
                    fragment = ShoppingFragment.newInstance(bundle);
                    toolbar.setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_Favorite:
                    fragment = new FavoriteFragment();
                    toolbar.setVisibility(View.VISIBLE);
                    break;
            }
            replaceFragment(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.log_out_box, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        alertDialog.show();
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        TextView btn_not_now = dialogView.findViewById(R.id.btn_not_now);
        TextView btn_yes = dialogView.findViewById(R.id.btn_yes);

        btn_not_now.setOnClickListener(view -> alertDialog.dismiss());

        btn_yes.setOnClickListener(v -> {
            //LISessionManager.getInstance(HomeActivity.this).clearSession();
     /*       SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();*/

          AppPreferences.savePreferences(HomeActivity.this, "LOGIN_STATUS","");
            LoginManager.getInstance().logOut();
            mAuth = FirebaseAuth.getInstance();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("819544222004-220uu2aku1ku7quel6butr9sl1i8t5as.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
            LoginManager.getInstance().logOut();
            AppPreferences.savePreferences(HomeActivity.this, "LOGIN_STATUS","");

            mAuth.signOut();
            mGoogleSignInClient.signOut();
            Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            startActivity(intent);
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

            alertDialog.dismiss();
        });

    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currLat = String.valueOf(location.getLatitude());
                        currLong = String.valueOf(location.getLongitude());
                        getAddress(location.getLatitude(), location.getLongitude());
                        break;
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);
            if (list != null && list.size() > 0) {
                address = list.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}