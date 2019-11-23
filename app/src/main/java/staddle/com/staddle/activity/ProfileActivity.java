package staddle.com.staddle.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.EditProfileResponse;
import staddle.com.staddle.ResponseClasses.UploadImageResponse;
import staddle.com.staddle.ResponseClasses.UserInfoResponse;
import staddle.com.staddle.bean.UserInfoModule;
import staddle.com.staddle.internetconnection.CheckNetwork;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.AppConstants;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog pd;
    ApiInterface apiInterface;
    String TAG = getClass().getSimpleName();
    ImageView iv_back, iv_upload_picture;
    RoundedImageView iv_user_profile_picture;
    EditText edt_user_name, edt_user_email, edt_user_mobile;
    String userId, userName, userEmail, userMobile, userProfilePicture;
    LinearLayout ll_btn_update_profile;

    // =========== Upload image ================
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 2;
    Bitmap bitmap;
    Bitmap compressedImageBitmap;
    Uri fileUri;
    Uri imageUrl;
    String profile_image;
    String imageString;
    String post_picture;
    boolean image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AppConstants.ChangeStatusBarColor(ProfileActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        userId = AppPreferences.loadPreferences(ProfileActivity.this, "USER_ID");

        find_All_IDs();

        if (CheckNetwork.isNetworkAvailable(ProfileActivity.this)) {
            // ======== Api Call =============
            getUserInfo(userId);
        } else {
            Toast.makeText(ProfileActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

        iv_back.setOnClickListener(view -> onBackPressed());

        ll_btn_update_profile.setOnClickListener(view -> doValidation());

        iv_upload_picture.setOnClickListener(view -> {
            // ======== Image Upload Data ==========
            showPictureDialog();
        });
    }// =================== End Of onCreate() ====================================

    private void find_All_IDs() {
        iv_back = findViewById(R.id.iv_back);
        iv_user_profile_picture = findViewById(R.id.iv_user_profile_picture);
        iv_upload_picture = findViewById(R.id.iv_upload_picture);
        edt_user_name = findViewById(R.id.edt_user_name);
        edt_user_email = findViewById(R.id.edt_user_email);
        edt_user_mobile = findViewById(R.id.edt_user_mobile);
        ll_btn_update_profile = findViewById(R.id.ll_btn_update_profile);

    }

    private void doValidation() {
        String userName = edt_user_name.getText().toString().trim();
        String userMobile = edt_user_mobile.getText().toString().trim();
        String userProfilePic = post_picture;
        if (userName.equalsIgnoreCase("")) {
            edt_user_name.setError("Please enter user name");
            edt_user_name.requestFocus();
        } else if (userMobile.equalsIgnoreCase("")) {
            edt_user_mobile.setError("Please enter user mobile no");
            edt_user_mobile.requestFocus();
        } else if (userProfilePic.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please add profile picture", Toast.LENGTH_SHORT).show();
        } else {
            if (CheckNetwork.isNetworkAvailable(ProfileActivity.this)) {
                // ======== Api Call =============
                editUserInfo(userId, userName, userMobile, userProfilePic);
            } else {
                Toast.makeText(ProfileActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ===================================*** Image Upload Data ***=================================
    private void showPictureDialog() {
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        pictureDialog.setIcon(R.drawable.ic_vector_camera);
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera", "Cancel"};

        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takeFromGallery();
                        break;
                    case 1:
                        takeFromCamera();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    private void takeFromCamera() {
        // Check if this device has a camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    // Open default camera
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);             // start the image capture Intent
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);        //100
        } else {
            // no camera on this device
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    private void takeFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MY_GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            image = true;
            imageUrl = data.getData();
            Uri filePath = data.getData();
            if (requestCode == 100 && resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUrl);
            }
            assert bitmap != null;
            uploadImage(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(Bitmap bitmap) {
        //converting image to base64 string
        bitmap = compressImage(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        if (CheckNetwork.isNetworkAvailable(ProfileActivity.this)) {
            //=========== Api Call ===============
            uploadImageCall(imageString);
        } else {
            Alerts.showAlert(ProfileActivity.this);
        }
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > 400) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        assert bitmap != null;
        return bitmap;
    }

    private void uploadImageCall(final String imageString) {
        pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("Uploading profile picture...");
        pd.setCancelable(false);
        pd.show();

        Call<UploadImageResponse> call = apiInterface.uploadPicture(imageString);

        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);
                UploadImageResponse uploadImageResponse = response.body();

                if (uploadImageResponse != null) {
                    String message = uploadImageResponse.getMessage();
                    String status = uploadImageResponse.getStatus();
                    String info = uploadImageResponse.getInfo();

                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        post_picture = info;
                        Picasso.get()
                                .load("http://staddle.in/mobileapp/post_image/" + info)
                                .error(R.drawable.user_profile)
                                .into(iv_user_profile_picture);

                    } else {
                        Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getUserInfo(String userId) {
        pd = new ProgressDialog(ProfileActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<UserInfoResponse> call = apiInterface.getUserInfo(userId);

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                pd.dismiss();
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
                                    post_picture = userInfoModule.getImage();
                                }

                                AppPreferences.savePreferences(ProfileActivity.this, "USER_NAME", userName);
                                AppPreferences.savePreferences(ProfileActivity.this, "USER_EMAIL", userEmail);
                                AppPreferences.savePreferences(ProfileActivity.this, "USER_PROFILE_PIC", post_picture);


                                edt_user_name.setText(userName);
                                edt_user_email.setText(userEmail);
                                edt_user_mobile.setText(userMobile);

                                if (post_picture.equalsIgnoreCase("") || post_picture.equalsIgnoreCase("null")) {
                                    Picasso.get()
                                            .load(R.drawable.user_profile)
                                            .error(R.drawable.user_profile)
                                            .into(iv_user_profile_picture);

                                } else if (post_picture.contains(".png")) {
                                    Picasso.get()
                                            .load("http://staddle.in/mobileapp/post_image/" + post_picture)
                                            .error(R.drawable.user_profile)
                                            .into(iv_user_profile_picture);

                                } else {
                                    Picasso.get()
                                            .load(post_picture)
                                            .error(R.drawable.user_profile)
                                            .into(iv_user_profile_picture);
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
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void editUserInfo(String userId, String userName, String userMobile, String userProfilePic) {
        pd = new ProgressDialog(ProfileActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<EditProfileResponse> call = apiInterface.editProfile(userId, userName, userMobile, userProfilePic);

        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        EditProfileResponse editProfileResponse = response.body();

                        if (editProfileResponse != null) {
                            String status = editProfileResponse.getStatus();
                            String message = editProfileResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
