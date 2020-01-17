package staddle.com.staddle.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import staddle.com.staddle.R;
import staddle.com.staddle.adapter.FetchAddressAdapter;
import staddle.com.staddle.bean.MySingleton;
import staddle.com.staddle.bean.SavedAddressList;
import staddle.com.staddle.fragment.ShoppingFragment;
import staddle.com.staddle.retrofitApi.EndApi;
import staddle.com.staddle.sheardPref.AppPreferences;

public class AllAddressActivity extends AppCompatActivity {



    // request Codes;
    public static final int GETSCHEDULEREQCODE=1;
    public static final int GETSCHEDULERESCODE=2;
    public static final String SELECTEDDATEKEY="date";
    public static final String SELECTEDTIMEKEY="time";
    String cid="";
    String SelectedDate="";
    String SelectedTime="";
    String FinalSetAddress="";
    String FinalSetNickname="";
    ImageView imageView;

    public static RecyclerView addressListRecycler;
    ImageView closeSheet;
    public static List<SavedAddressList> savedAddressLists;
    public static FetchAddressAdapter fetchAddressAdapter;
    TextView AddNewAddress;
    TextView SearchAddress;
    private static ShimmerFrameLayout shimmerFrameLayout;
    public static RelativeLayout noaddressfoundlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_address);
        Intent intent=getIntent();
        cid=intent.getStringExtra("cid");
        init();
        AddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SelectDeliveryAddress.class);
                startActivityForResult(intent, ShoppingFragment.REQUESTCODEREFRES);
            }
        });
        SearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SearchAddressActivity.class);
                startActivityForResult(intent, ShoppingFragment.REQUESTCODEREFRES);

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void init(){
        savedAddressLists=new ArrayList<>();

        shimmerFrameLayout=findViewById(R.id.shimmerviewaddresses);
        AddNewAddress=findViewById(R.id.addnewunderSavedList);
        SearchAddress = findViewById(R.id.addnewunderSavedListviasearch);
        imageView = findViewById(R.id.back_btn);
        addressListRecycler=findViewById(R.id.savedaddress);
        noaddressfoundlayout=findViewById(R.id.noaddressfoundlayout);
        addressListRecycler.setLayoutManager(new LinearLayoutManager(AllAddressActivity.this));

        String uid = AppPreferences.loadPreferences(getApplicationContext(), "USER_ID");
        fetchpromocodes(uid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        fetchAddressAdapter.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GETSCHEDULEREQCODE) {
           SelectedDate=data.getStringExtra(SELECTEDDATEKEY);
           SelectedTime=data.getStringExtra(SELECTEDTIMEKEY);
           FinalSetAddress=data.getStringExtra("address");
           FinalSetNickname=data.getStringExtra("nickname");

           if(SelectedTime.equals("") && SelectedDate.equals("")){
               Toast.makeText(this, "Timing and day for services required", Toast.LENGTH_SHORT).show();
           }else{
               Intent intent= new Intent();
               intent.putExtra("date", SelectedDate);
               intent.putExtra("time",SelectedTime);
               intent.putExtra("nickname",FinalSetNickname);
               intent.putExtra("address",FinalSetAddress);
               setResult(ShoppingFragment.CAT1RESCODEHOME, intent);
               finish();
           }
        }

    }

    public void fetchpromocodes(String uid){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndApi.FETCH_SAVED_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.e("response",response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String nickName = jsonObject.getString("NICKNAME");
                                    String addressString = jsonObject.getString("ADDRESS");
                                    savedAddressLists.add(new SavedAddressList(nickName, addressString));
                                }

                                fetchAddressAdapter = new FetchAddressAdapter(savedAddressLists, AllAddressActivity.this);
                                addressListRecycler.setAdapter(fetchAddressAdapter);
                                if(savedAddressLists.size()==0){
                                    noaddressfoundlayout.setVisibility(View.VISIBLE);
                                }
                                if(savedAddressLists.size()!=0){
                                    noaddressfoundlayout.setVisibility(View.GONE);

                                }
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);







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
                params.put("UID",uid);


                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

}
