package staddle.com.staddle.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.BuildConfig;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.MyOrderListResponse;
import staddle.com.staddle.ResponseClasses.UserInfoResponse;
import staddle.com.staddle.activity.AboutUsActivity;
import staddle.com.staddle.activity.HelpActivity;
import staddle.com.staddle.activity.LocationActivity;
import staddle.com.staddle.activity.LoginActivity;
import staddle.com.staddle.activity.MobileLoginActivity;
import staddle.com.staddle.activity.PaymentMethodsActivtiy;
import staddle.com.staddle.activity.PolicyActivity;
import staddle.com.staddle.activity.ProfileActivity;
import staddle.com.staddle.adapter.MyOrderListAdapter;
import staddle.com.staddle.bean.MyOrderListModel;
import staddle.com.staddle.bean.UserInfoModule;
import staddle.com.staddle.fcm.DBManager;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

import static android.graphics.Color.TRANSPARENT;
import static staddle.com.staddle.HomeActivity.mAuth;
import static staddle.com.staddle.HomeActivity.mGoogleSignInClient;
import static staddle.com.staddle.HomeActivity.toolbar;
import static staddle.com.staddle.activity.VendorDetailsActivityNew.CR_OR_META_DATA;

public class ProfileFragment extends Fragment {
    View view;
    Context mContext;
    ApiInterface apiInterface;
    String TAG = getClass().getSimpleName();
    String userId;
    private String userEmail;
    private String userName;
    private String contactNumber;
    private TextView username;
    private TextView number;
    private RelativeLayout myfavLayout;
    Activity activity;
    private RelativeLayout helpLayout;
    private RelativeLayout MyOrdersRcvLayout;
    ImageView dropdown;
    ImageView dropup;

    ArrayList<MyOrderListModel> myOrderListModelArrayList;
    ArrayList<MyOrderListModel.Data> dataArrayList;
    MyOrderListAdapter myOrderListAdapter;
    RecyclerView myorderlist;
    ShimmerFrameLayout shimmerFrameLayout;
    private ProgressDialog progressDialog;
    FragmentManager fragmentManager;

    RelativeLayout inviteFriendsLayout;
    RelativeLayout policyLayout;
    RelativeLayout aboutUsLayout;
    RelativeLayout layoutLogout;
    LinearLayout contentlayout;
    RelativeLayout payment_methods;


    TextView appVersionName;

    public ProfileFragment() {
        activity=(Activity)getContext();
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        mContext = getActivity();


        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        userId = AppPreferences.loadPreferences(getContext(), "USER_ID");

        find_All_IDs(view);

        if (CheckNetwork.isNetworkAvailable(getContext())) {
            // ======== Api Call =============
            getUserInfo(userId);
            getMyOrderList(userId);
        } else {
            Toast.makeText(getContext(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

        myfavLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new FavoriteFragment());
                toolbar.setVisibility(View.VISIBLE);
            }
        });

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrdersRcvLayout.setVisibility(View.VISIBLE);
                dropup.setVisibility(View.VISIBLE);
                dropdown.setVisibility(View.GONE);
            }
        });
        dropup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrdersRcvLayout.setVisibility(View.GONE);
                dropdown.setVisibility(View.VISIBLE);
                dropup.setVisibility(View.GONE);
            }
        });
        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HelpActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        inviteFriendsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        policyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PolicyActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        payment_methods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PaymentMethodsActivtiy.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void find_All_IDs(View view) {
        //  rlv_house_keeping = view.findViewById(R.id.rlv_house_keeping);
        username=view.findViewById(R.id.usernameprofile);
        number=view.findViewById(R.id.emailcontactprofile);
        myorderlist=view.findViewById(R.id.RecyclerviewMyOrders);
        myorderlist.setLayoutManager(new LinearLayoutManager(getContext()));
        shimmerFrameLayout=view.findViewById(R.id.shimmerviewprofile);
        myOrderListModelArrayList=new ArrayList<>();
        dataArrayList=new ArrayList<>();
        myfavLayout=view.findViewById(R.id.myfavlayourprofile);
        fragmentManager=getActivity().getSupportFragmentManager();
        helpLayout=view.findViewById(R.id.helpIntenttxt);
        MyOrdersRcvLayout = view.findViewById(R.id.myordersRecyclerlayoutProfile);
        dropdown = view.findViewById(R.id.arrowdropdownMyOrders);
        dropup = view.findViewById(R.id.arrowdropupMyOrders);
        inviteFriendsLayout=view.findViewById(R.id.inviteFriendsLayout);
        policyLayout=view.findViewById(R.id.layoutPolicy);
        aboutUsLayout=view.findViewById(R.id.aboutUsLayout);
        layoutLogout=view.findViewById(R.id.layoutlLogout);
        appVersionName=view.findViewById(R.id.appVersionName);
        String versionName = BuildConfig.VERSION_NAME;
        appVersionName.setText("App version : "+versionName);
        contentlayout = view.findViewById(R.id.layoutprofilecontent);
        payment_methods = view.findViewById(R.id.payment_methods);
        DBManager db = new DBManager(getContext());
//        db.open();
//        db.truncate();
//        db.close();
//        AppPreferences.deletePref(getContext(),CR_OR_META_DATA);



    }
    private void getUserInfo(String userId) {



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
                                    contactNumber = userInfoModule.getMobile();
                                }

                                AppPreferences.savePreferences(getContext(), "USER_NAME", userName);
                                AppPreferences.savePreferences(getContext(), "USER_EMAIL", userEmail);



                                username.setText(userName);
                                number.setText(contactNumber+"  |  "+userEmail);


//                                if (post_picture.equalsIgnoreCase("") || post_picture.equalsIgnoreCase("null")) {
//                                    Picasso.get()
//                                            .load(R.drawable.user_profile)
//                                            .error(R.drawable.user_profile)
//                                            .into(iv_user_profile_picture);
//
//                                } else if (post_picture.contains(".png")) {
//                                    Picasso.get()
//                                            .load("http://staddle.in/mobileapp/post_image/" + post_picture)
//                                            .error(R.drawable.user_profile)
//                                            .into(iv_user_profile_picture);
//
//                                } else {
//                                    Picasso.get()
//                                            .load(post_picture)
//                                            .error(R.drawable.user_profile)
//                                            .into(iv_user_profile_picture);
//                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

                Toast.makeText(getContext(), "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getMyOrderList(String uid) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MyOrderListResponse> call = apiInterface.getMyOrderListUser(uid);

        call.enqueue(new Callback<MyOrderListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderListResponse> call, @NonNull Response<MyOrderListResponse> response) {

                try {
                    if (response.isSuccessful()) {
                        myOrderListModelArrayList.clear();
                        MyOrderListResponse myOrderListResponse = response.body();
                        Log.d("MYORDERRESPONSE",response+"");
                        if (myOrderListResponse != null) {
                            String status = myOrderListResponse.getStatus();
                            String message = myOrderListResponse.getMessage();
                            if (status.equalsIgnoreCase("1")) {
                                myOrderListModelArrayList = myOrderListResponse.getInfo();
                                myOrderListAdapter = new MyOrderListAdapter(getContext(), myOrderListModelArrayList);
                                myorderlist.setAdapter(myOrderListAdapter);
                                myorderlist.setVisibility(View.VISIBLE);
                                myorderlist.setHasFixedSize(true);
                                myOrderListAdapter.notifyDataSetChanged();
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                contentlayout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                                //rl_no.setVisibility(View.VISIBLE);
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                myorderlist.setVisibility(View.GONE);
                                contentlayout.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Response Fail !!", Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        contentlayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyOrderListResponse> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    public void share() {
        //To automatically fill in the application name and application id you could use this:
        int applicationNameId = getActivity().getApplicationInfo().labelRes;
        final String appPackageName = getActivity().getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, this.getString(applicationNameId));
        String text = "Install this cool application: ";
        String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
        i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
        startActivity(Intent.createChooser(i, "Share link:"));
    }
    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

            AppPreferences.savePreferences(getContext(), "LOGIN_STATUS","");
            LoginManager.getInstance().logOut();
            mAuth = FirebaseAuth.getInstance();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("819544222004-220uu2aku1ku7quel6butr9sl1i8t5as.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
            LoginManager.getInstance().logOut();
            AppPreferences.savePreferences(getContext(), "LOGIN_STATUS","");

            mAuth.signOut();
            mGoogleSignInClient.signOut();
            Intent intent = new Intent(getContext(), MobileLoginActivity.class);
           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

            alertDialog.dismiss();
        });

    }


}
