package staddle.com.staddle.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.LoginResponse;
import staddle.com.staddle.ResponseClasses.MediaLoginResponse;
import staddle.com.staddle.bean.LoginInfoModel;
import staddle.com.staddle.bean.MediaInfoModule;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ProgressDialog pd;
    String TAG = getClass().getSimpleName();
    TextView tv_btn_forgot_password, tv_new_user_registration;
    LinearLayout ll_btn_login, ll_google, ll_linked_in, ll_facebook;
    EditText edt_phone_or_email, edt_password;
    ApiInterface apiInterface;
    String userId, userName, userEmail, userMobile;

    public static final String PACKAGE = "staddle.com.staddle";
    CallbackManager callbackManager;
    String facebook_id, facebook_profile_pic, facebook_name, facebook_email, facebook_mobile;
    boolean ll_googleOnClick, ll_facebookOnClick, ll_inkedInOnClick;
    private GoogleSignInClient mGoogleSignInClient;
    private int SIGN_IN = 30;
    String device_token;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppConstants.ChangeStatusBarColor(LoginActivity.this);

        device_token = AppPreferences.loadPreferences(LoginActivity.this, "DEVICE_TOKEN");

       /* if (AppPreferences.loadPreferences(LoginActivity.this, "isLogin").equalsIgnoreCase("Y")) {
            Login();
        }*/

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        find_All_IDs();

        initGoogleAPIClient();

        generateHashKey();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        ll_btn_login.setOnClickListener(view -> doValidation());

        tv_btn_forgot_password.setOnClickListener(view -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent, options.toBundle());
        });

        tv_new_user_registration.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        });

        ll_google.setOnClickListener(v -> {
            ll_googleOnClick = true;
            if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, SIGN_IN);
            } else {
                Alerts.showAlert(LoginActivity.this);
            }

        });

        ll_linked_in.setOnClickListener(v -> {
            ll_inkedInOnClick = true;
            LISessionManager.getInstance(getApplicationContext())
                    .init(LoginActivity.this, buildScope(), new AuthListener() {
                        @Override
                        public void onAuthSuccess() {
                            if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                                fetchLinkedInInfo();
                            } else {
                                Alerts.showAlert(LoginActivity.this);
                            }
                        }

                        @Override
                        public void onAuthError(LIAuthError error) {
                            Toast.makeText(getApplicationContext(), "Connection Not available failed ", Toast.LENGTH_LONG).show();
                        }
                    }, true);
        });

        ll_facebook.setOnClickListener(v -> {
            ll_facebookOnClick = true;
            AppPreferences.savePreferences(LoginActivity.this, "CHECK_MEDIA_LOGIN", "facebook");
            if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email"));//user_birthday,user_location
                facebookLogin();
            } else {
                Alerts.showAlert(LoginActivity.this);
            }
        });

    }

    private void find_All_IDs() {
        ll_btn_login = findViewById(R.id.ll_btn_login);
        ll_google = findViewById(R.id.ll_google);
        ll_linked_in = findViewById(R.id.ll_linked_in);
        ll_facebook = findViewById(R.id.ll_facebook);
        tv_btn_forgot_password = findViewById(R.id.tv_btn_forgot_password);
        tv_new_user_registration = findViewById(R.id.tv_new_user_registration);
        edt_phone_or_email = findViewById(R.id.edt_phone_or_email);
        edt_password = findViewById(R.id.edt_password);
    }

    private void Login() {
        finish();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void initGoogleAPIClient() {
        mAuth = FirebaseAuth.getInstance();
        String webClientId = getString(R.string.default_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               //.requestIdToken("83197153920-7mnqes1l663jnv9c8759erh5sap8nt6v.apps.googleusercontent.com")
               .requestIdToken("819544222004-220uu2aku1ku7quel6butr9sl1i8t5as.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            final GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            final String google_id = acct.getId();
            String google_userName = acct.getDisplayName();
            String google_email = acct.getEmail();
            String google_mobile = "123456789";
            String google_profilePic = String.valueOf(acct.getPhotoUrl());
            AppPreferences.savePreferences(LoginActivity.this, "USER_NAME", google_userName);
            AppPreferences.savePreferences(LoginActivity.this, "USER_EMAIL", google_email);
            AppPreferences.savePreferences(LoginActivity.this, "USER_PROFILE_PIC", google_profilePic);

            if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                socialGoogleLogin(google_id, google_userName, google_email, google_mobile, google_profilePic,device_token,"A");
            } else {
                Alerts.showAlert(LoginActivity.this);
            }
        }
    }

    public void generateHashKey() {
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG + "Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.d("Error", e.getMessage(), e);
        }
    }

    //for linkedin
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void fetchLinkedInInfo() {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Loading Please Wait...");
        pd.setCancelable(false);
        pd.show();
        // String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name)";
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,public-profile-url,picture-url,email-address,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(LoginActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                pd.dismiss();
                Log.d(TAG + "Response >>>> :", String.valueOf(apiResponse));
                try {
                    String linkedIn_ID = jsonObject.getString("id");
                    String firstName = jsonObject.getString("firstName");
                    String lastName = jsonObject.getString("lastName");
                    String linkedIn_userName = firstName + " " + lastName;
                    String linkedInEmail = jsonObject.getString("emailAddress");
                    String linkedInMobile = "123456789";
                    String linkedIn_Profile_pic = jsonObject.getString("pictureUrl");

                    if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                        // ========== Api Call =============
                        socialLinkedInLogin(linkedIn_ID, linkedIn_userName, linkedInEmail, linkedInMobile, linkedIn_Profile_pic);
                    } else {
                        Alerts.showAlert(LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error found! Please retry...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                Toast.makeText(LoginActivity.this, "Error found! Please retry...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ONSUCCESS", "User ID: " + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {
                            try {
                                Log.e("object", object.toString());
                                try {
                                    facebook_id = object.getString("id");
                                } catch (Exception e) {
                                    facebook_id = "";
                                    e.printStackTrace();
                                }
                                try {
                                    facebook_name = object.getString("name");
                                } catch (Exception e) {
                                    facebook_name = "";
                                    e.printStackTrace();
                                }
                                try {
                                    facebook_email = object.getString("email");
                                } catch (Exception e) {
                                    facebook_email = "";
                                    e.printStackTrace();
                                }
                                try {
                                    facebook_mobile = "123456789";
                                } catch (Exception e) {
                                    facebook_mobile = "";
                                    e.printStackTrace();
                                }
                                profilePicInfo();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> {
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile,email"));
                    facebookLogin();
                }).executeAsync();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");
                Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_LONG).show();
                AccessToken.setCurrentAccessToken(null);
            }
        });
//        APP ID: 800414636999322
    }

    private void profilePicInfo() {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/picture",
                params,
                HttpMethod.GET,
                response -> {
                    Log.e("Response 2", response + "");
                    try {
                        facebook_profile_pic = (String) response.getJSONObject().getJSONObject("data").get("url");
                        Log.e("Picture", facebook_profile_pic);

                        if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                            socialFacebookLogin(facebook_id, facebook_name, facebook_email, facebook_mobile, facebook_profile_pic,device_token,"A");
                        } else {
                            Alerts.showAlert(LoginActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (ll_inkedInOnClick) {
                ll_googleOnClick = false;
                ll_facebookOnClick = false;
                LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
                DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
                deepLinkHelper.onActivityResult(this, requestCode, resultCode, data);
            } else if (ll_facebookOnClick) {
                ll_googleOnClick = false;
                ll_inkedInOnClick = false;
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } else if (ll_googleOnClick) {
                if (requestCode == SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);

                  /*  Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        assert account != null;
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        Log.w(TAG, "Google sign in failed", e);
                    }*/
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
    }

    private void doValidation() {
        String email = edt_phone_or_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        AppPreferences.savePreferences(LoginActivity.this, "EMAIL", email);
        AppPreferences.savePreferences(LoginActivity.this, "PASSWORD", password);

        if (email.equalsIgnoreCase("")) {
            edt_phone_or_email.setError("Enter Email or Phone No.");
            edt_phone_or_email.requestFocus();
        } else if (email.matches("[0-9]+") && email.length() < 10) {
            edt_phone_or_email.setError("Enter 10 digit Phone No.");
            edt_phone_or_email.requestFocus();
        } else if (email.length() <= 10) {
            if (password.equalsIgnoreCase("")) {
                edt_password.setError("Enter Password.");
                edt_password.requestFocus();
            } else if (password.length() < 5) {
                edt_password.setError("Password is too short !! ");
                edt_password.requestFocus();
            } else {
                if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                    doLogin(email, password, device_token);
                } else {
                    Toast.makeText(LoginActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_phone_or_email.setError("Enter valid email id.");
            edt_phone_or_email.requestFocus();
        } else if (password.equalsIgnoreCase("")) {
            edt_password.setError("Enter Password.");
            edt_password.requestFocus();
        } else if (password.length() < 5) {
            edt_password.setError("Password is too short !! ");
            edt_password.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                doLogin(email, password, device_token);
            } else {
                Toast.makeText(LoginActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doLogin(String email, String password, String device_id) {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<LoginResponse> call = apiInterface.UserLogin(email, password, device_id, "A");

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();

                        if (loginResponse != null) {
                            ArrayList<LoginInfoModel> loginInfoModelArrayList = loginResponse.getInfo();

                            String status = loginResponse.getStatus();
                            String message = loginResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {

                                for (LoginInfoModel loginInfoModel : loginInfoModelArrayList) {
                                    userId = loginInfoModel.getUid();
                                    userName = loginInfoModel.getUsername();
                                    userEmail = loginInfoModel.getEmail();
                                    userMobile = loginInfoModel.getMobile();
                                }

                                AppPreferences.savePreferences(LoginActivity.this, "USER_ID", userId);
                                AppPreferences.savePreferences(LoginActivity.this, "USER_NAME", userName);
                                AppPreferences.savePreferences(LoginActivity.this, "USER_EMAIL", userEmail);
                                AppPreferences.savePreferences(LoginActivity.this, "LOGIN_STATUS", "1");
                                AppPreferences.savePreferences(LoginActivity.this, "isLogin", "Y");

                                Toast.makeText(LoginActivity.this, message + " !!", Toast.LENGTH_SHORT).show();
                                Login();
                            /*   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);*/
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                // finish();

                            } else if (status.equalsIgnoreCase("2")) {
                                Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog_active_user_login, null);
                                builder.setView(dialogView);
                                builder.setCancelable(true);

                                final AlertDialog alertDialog1 = builder.create();
                                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog1.show();

                                TextView btn_ok1 = dialogView.findViewById(R.id.btn_ok1);
                                TextView tv_msg_body = dialogView.findViewById(R.id.tv_msg_body);
                                tv_msg_body.setText(message);

                                btn_ok1.setOnClickListener(view -> alertDialog1.dismiss());
                            }
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, response.code() + "Server Code Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void socialGoogleLogin(String google_id, String google_userName, String google_email, String google_mobile, String google_profilePic,String device_id,String device_type) {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<MediaLoginResponse> call = apiInterface.socialGoogleLogin(google_id, google_userName, google_email, google_mobile, google_profilePic,device_id,device_type);

        call.enqueue(new Callback<MediaLoginResponse>() {
            @Override
            public void onResponse(Call<MediaLoginResponse> call, Response<MediaLoginResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);

                if (response.isSuccessful()) {
                    MediaLoginResponse mediaLoginResponse = response.body();

                    if (mediaLoginResponse != null) {
                        ArrayList<MediaInfoModule> mediaInfoModuleArrayList = mediaLoginResponse.getInfo();

                        String status = mediaLoginResponse.getStatus();
                        String message = mediaLoginResponse.getMessage();

                        if (status.equalsIgnoreCase("1")) {
                            for (MediaInfoModule mediaInfoModule : mediaInfoModuleArrayList) {
                                userId = mediaInfoModule.getUid();
                                userName = mediaInfoModule.getUsername();
                                userEmail = mediaInfoModule.getEmail();
                                userMobile = mediaInfoModule.getMobile();
                            }

                            AppPreferences.savePreferences(LoginActivity.this, "USER_ID", userId);
                            AppPreferences.savePreferences(LoginActivity.this, "USER_NAME", userName);
                            AppPreferences.savePreferences(LoginActivity.this, "USER_EMAIL", userEmail);
                            AppPreferences.savePreferences(LoginActivity.this, "LOGIN_STATUS", "1");

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MediaLoginResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "Server Error :" + t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void socialLinkedInLogin(String linkedIn_ID, String linkedIn_userName, String linkedInEmail, String linkedInMobile, String linkedIn_Profile_pic) {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<MediaLoginResponse> call = apiInterface.socialLinkedInLogin(linkedIn_ID, linkedIn_userName, linkedInEmail, linkedInMobile, linkedIn_Profile_pic);

        call.enqueue(new Callback<MediaLoginResponse>() {
            @Override
            public void onResponse(Call<MediaLoginResponse> call, Response<MediaLoginResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);

                if (response.isSuccessful()) {
                    MediaLoginResponse mediaLoginResponse = response.body();

                    if (mediaLoginResponse != null) {
                        ArrayList<MediaInfoModule> mediaInfoModuleArrayList = mediaLoginResponse.getInfo();

                        String status = mediaLoginResponse.getStatus();
                        String message = mediaLoginResponse.getMessage();

                        if (status.equalsIgnoreCase("1")) {
                            for (MediaInfoModule mediaInfoModule : mediaInfoModuleArrayList) {
                                userId = mediaInfoModule.getUid();
                                userName = mediaInfoModule.getUsername();
                                userEmail = mediaInfoModule.getEmail();
                                userMobile = mediaInfoModule.getMobile();
                            }

                            AppPreferences.savePreferences(LoginActivity.this, "USER_ID", userId);
                            AppPreferences.savePreferences(LoginActivity.this, "USER_NAME", userName);
                            AppPreferences.savePreferences(LoginActivity.this, "USER_EMAIL", userEmail);
                            AppPreferences.savePreferences(LoginActivity.this, "LOGIN_STATUS", "1");

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, response.code() + "Server Code Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MediaLoginResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void socialFacebookLogin(String facebook_id, String facebook_name, String facebook_email, String facebook_mobile, String facebook_profile_pic,String device_id,String device_type) {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<MediaLoginResponse> call = apiInterface.socialFacebookLogin(facebook_id, facebook_name, facebook_email, facebook_mobile, facebook_profile_pic,device_id,device_type);

        call.enqueue(new Callback<MediaLoginResponse>() {
            @Override
            public void onResponse(Call<MediaLoginResponse> call, Response<MediaLoginResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);

                if (response.isSuccessful()) {
                    MediaLoginResponse mediaLoginResponse = response.body();

                    if (mediaLoginResponse != null) {

                        ArrayList<MediaInfoModule> mediaInfoModuleArrayList = mediaLoginResponse.getInfo();

                        String status = mediaLoginResponse.getStatus();
                        String message = mediaLoginResponse.getMessage();

                        if (status.equalsIgnoreCase("1")) {

                            for (MediaInfoModule mediaInfoModule : mediaInfoModuleArrayList) {
                                userId = mediaInfoModule.getUid();
                                userName = mediaInfoModule.getUsername();
                                userEmail = mediaInfoModule.getEmail();
                                userMobile = mediaInfoModule.getUid();
                            }

                            AppPreferences.savePreferences(LoginActivity.this, "USER_ID", userId);
                            AppPreferences.savePreferences(LoginActivity.this, "USER_NAME", userName);
                            AppPreferences.savePreferences(LoginActivity.this, "USER_EMAIL", userEmail);
                            AppPreferences.savePreferences(LoginActivity.this, "LOGIN_STATUS", "1");

                            Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

                        } else {
                            Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, response.code() + "Server Code Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<MediaLoginResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

/*
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        final String google_id = user.getUid();
                        String google_userName = user.getDisplayName();
                        String google_email = user.getEmail();
                        String google_mobile = "123456789";
                        String google_profilePic = String.valueOf(user.getPhotoUrl());
                        AppPreferences.savePreferences(LoginActivity.this, "USER_NAME", google_userName);
                        AppPreferences.savePreferences(LoginActivity.this, "USER_EMAIL", google_email);
                        AppPreferences.savePreferences(LoginActivity.this, "USER_PROFILE_PIC", google_profilePic);

                        if (CheckNetwork.isNetworkAvailable(LoginActivity.this)) {
                            socialGoogleLogin(google_id, google_userName, google_email, google_mobile, google_profilePic,device_token,"A");
                        } else {
                            Alerts.showAlert(LoginActivity.this);
                        }
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }
*/

}


