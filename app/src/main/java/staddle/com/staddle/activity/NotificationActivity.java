package staddle.com.staddle.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.adapter.FetchAddressAdapter;
import staddle.com.staddle.adapter.NotificationsAdapter;
import staddle.com.staddle.bean.MySingleton;
import staddle.com.staddle.bean.NotificationsDataModels;
import staddle.com.staddle.bean.SavedAddressList;
import staddle.com.staddle.retrofitApi.EndApi;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.AppConstants;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class NotificationActivity extends AppCompatActivity {

    ImageView iv_back;
    RelativeLayout rv_booking_status, rv_wishlist_status;

    RecyclerView notifications;
    ArrayList<NotificationsDataModels>  notificationsDataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        AppConstants.ChangeStatusBarColor(NotificationActivity.this);

        find_All_ID();

        getnotifications(AppPreferences.loadPreferences(NotificationActivity.this, "USER_ID"));

        iv_back.setOnClickListener(view -> onBackPressed());

        rv_booking_status.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, MyOrderActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        });

        rv_wishlist_status.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, MyWishListActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        });
    }

    private void find_All_ID() {
        iv_back = findViewById(R.id.iv_back);
        rv_booking_status = findViewById(R.id.rv_booking_status);
        rv_wishlist_status = findViewById(R.id.rv_wishlist_status);
        notifications = findViewById(R.id.notificationsRecycler);
        notificationsDataModels = new ArrayList<>();
        notifications.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void getnotifications(String uid){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndApi.FETCH_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.e("response",response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                notificationsDataModels.add(new NotificationsDataModels(jsonObject.getString("content"),jsonObject.getString("order_status")));
                            }
                            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(NotificationActivity.this,notificationsDataModels);
                            notifications.setAdapter(notificationsAdapter);
                            notifications.hasFixedSize();








                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("responce", error.toString() );
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Connection failed!")
                        .setCancelable(false)
                        .setMessage("Please check your internet connection or restart the App!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                // params.put("total",totalprice);
                params.put("uid",uid);


                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);

//        notificationsDataModels.add(new NotificationsDataModels("Now save 20% on your first order!","New user? Get 20% off on your first order at any store.","0"));
//        notificationsDataModels.add(new NotificationsDataModels("Xyz Salon and Spa confirmed your order.","Your booking for 1 items at home at Xyz Salon and Spa has been confirmed.","0"));
//        notificationsDataModels.add(new NotificationsDataModels("Service Delivered!","Your booking for 1 items at home at Xyz Salon and Spa has been been delivered today at 2.30 pm at your home.Tap to rate Xyz Salon and Spa.","0"));

    }
}