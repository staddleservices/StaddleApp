package staddle.com.staddle.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.internetconnection.NetworkAvailablity;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;


public class LocationActivity extends AppCompatActivity implements LocationListener {

    public static EditText edt_search_location;

    Button next;
    CardView currentLocation;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Bundle bundle;
    Location mlastLocation;
    String culocation = "";

    Geocoder geocoder;
    private List<Address> list;
    Context mContext;
    String currLat, currLong;

    //forword to home activity
    String username;
    String uid;
    String email;
    String profilepic;
    String mobile_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        AppConstants.ChangeStatusBarColor(LocationActivity.this);

        find_All_IDs();

        next.setOnClickListener(v -> {
            culocation = edt_search_location.getText().toString().trim();
            if (culocation.equalsIgnoreCase("")) {
                Toast.makeText(LocationActivity.this, "Please select location", Toast.LENGTH_LONG).show();
            } else {
                AppPreferences.savePreferences(LocationActivity.this, "LOCATION", culocation);
                AppPreferences.savePreferences(LocationActivity.this, "LATTITUDE", currLat);
                AppPreferences.savePreferences(LocationActivity.this, "LONGITUDE", currLong);
                AppPreferences.savePreferences(LocationActivity.this, "USER_ID", uid);
                AppPreferences.savePreferences(LocationActivity.this, "USER_NAME", username);
                AppPreferences.savePreferences(LocationActivity.this, "USER_EMAIL", email);
                AppPreferences.savePreferences(LocationActivity.this, "LOGIN_STATUS", "1");
                AppPreferences.savePreferences(LocationActivity.this, "USER_MOBILE", mobile_number);
                AppPreferences.savePreferences(LocationActivity.this, "isLogin", "Y");
                Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        currentLocation.setOnClickListener(v -> {

            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert manager != null;
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (statusOfGPS) {
                getCurrentLocation();
            } else {
                buildAlertMessageNoGps();
            }
        });
        edt_search_location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (NetworkAvailablity.chkStatus(LocationActivity.this)) {

                        Intent intent = new Intent(LocationActivity.this,SelectFromMapAddressActivity.class);
                        startActivity(intent);

                } else {
                    Toast.makeText(LocationActivity.this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        edt_search_location.setOnClickListener(v -> {
//            if (NetworkAvailablity.chkStatus(LocationActivity.this)) {
//                try {
//                    mContext = LocationActivity.this;
//                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build((Activity) mContext);
//                    startActivityForResult(intent, 1);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(LocationActivity.this, "Check Internet Connection !", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void find_All_IDs() {

        Intent intent=getIntent();
        uid=intent.getStringExtra("USER_ID");
        username=intent.getStringExtra("USER_NAME");
        email=intent.getStringExtra("USER_EMAIL");
        profilepic=intent.getStringExtra("USER_PROFILE_PIC");
        mobile_number = intent.getStringExtra("MOBILE");

        edt_search_location = findViewById(R.id.edt_search_location);

        currentLocation = findViewById(R.id.tv_current_location);
        next=findViewById(R.id.nextBtnLocation);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(LocationActivity.this, data);
                currLat = String.valueOf(place.getLatLng().latitude);
                currLong = String.valueOf(place.getLatLng().longitude);
                geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                try {
                    list = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    if (list != null && list.size() > 0) {
                        String address = list.get(0).getAddressLine(0);
                        String city = list.get(0).getLocality();
                        edt_search_location.setText(address);
                        Log.e("UsersCurrentLocation",address);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mlastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(LocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(LocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(LocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    Toast.makeText(LocationActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
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

    private void buildAlertMessageNoGps() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 55))
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                });
        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void getAddress(double latitude, double longitude) {
        String address = "";
        geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
        try {
            list = geocoder.getFromLocation(latitude, longitude, 1);
            if (list != null && list.size() > 0) {
                address = list.get(0).getAddressLine(0);
                String city = list.get(0).getLocality();
                String state = list.get(0).getAdminArea();
                String country = list.get(0).getCountryName();
                String postalCode = list.get(0).getPostalCode();
                String knownName = list.get(0).getFeatureName();
                edt_search_location.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LocationActivity.this, TermsAndConditionActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
