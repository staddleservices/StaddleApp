package staddle.com.staddle.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.adapter.PomoCodeAdapter;
import staddle.com.staddle.bean.MySingleton;
import staddle.com.staddle.bean.PromoList;
import staddle.com.staddle.fragment.ShoppingFragment;
import staddle.com.staddle.retrofitApi.EndApi;

import static staddle.com.staddle.fragment.ShoppingFragment.appliedpomodes;
import static staddle.com.staddle.fragment.ShoppingFragment.appliedpromovalue;
import static staddle.com.staddle.fragment.ShoppingFragment.tv_item_total;

public class PromoCodeActivity extends AppCompatActivity {

    RecyclerView promolistRecycler;
    List<PromoList> promoCodeList;
    ProgressDialog progressDialog;
    EditText searchPromoCode;
    TextView searchApplybtn;


    public static AlertDialog quantAlert;
    public static String totalprice;
    String ttlpriceafterdiscount;
    String vid;
    public static float discount;
    public static String promovalue="";
    ImageView backbtnpromoact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        init();
        fetchpromocodes(vid,totalprice);

        backbtnpromoact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchApplybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promoname=searchPromoCode.getText().toString();
                searchpromocode(promoname,totalprice);
                progressDialog.show();
            }
        });
    }

    private void init(){
        Intent intent=getIntent();
        totalprice=intent.getStringExtra("total");

        Log.d("TOTALPRICE",totalprice);
        vid=intent.getStringExtra("vid");
        //Log.d("VID",vid);
        promoCodeList=new ArrayList<>();
        backbtnpromoact = findViewById(R.id.backbtnpromoact);
        promolistRecycler=findViewById(R.id.promocodeslist);
        searchPromoCode=findViewById(R.id.searchpromocode_edittext);
        searchApplybtn=findViewById(R.id.searchApplybtn);
        promolistRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressDialog=new ProgressDialog(PromoCodeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void fetchpromocodes(String vid, String totalprice){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, EndApi.FETCH_COUPONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.e("response",response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String status = jsonObject.getString("status");
                                //if(Integer.parseInt(status)==1){
                                    //CreateDialogBoxError();}else
                                if(Integer.parseInt(status)==0){
                                    String promoname = jsonObject.getString("promonames");
                                    String promovalue  = jsonObject.getString("promovalue");
                                    String mprice  = jsonObject.getString("mprice");
                                    String description  = jsonObject.getString("description");

                                    promoCodeList.add(new PromoList(promoname,promovalue,mprice,description));
                                }else{
                                    CreateDialogBoxError();
                                }



                            }

                            PomoCodeAdapter pomoCodeAdapter=new PomoCodeAdapter(promoCodeList,PromoCodeActivity.this);
                            promolistRecycler.setAdapter(pomoCodeAdapter);
                            progressDialog.dismiss();



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
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
               // params.put("total",totalprice);
                params.put("vid",vid);
                params.put("totalprice",totalprice);


                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    private void searchpromocode(String promoname, String totalprices) {
        //Api call for searching entered promocode
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndApi.SEARCH_COUPONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.e("response", response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String status = jsonObject.getString("status");
                                if (Integer.parseInt(status) == 1) {
                                    //nopromofound
                                    CreateDialogBoxError();
                                } else if (Integer.parseInt(status) == 0) {
                                    String promoname = jsonObject.getString("promonames");
                                    String promovalue = jsonObject.getString("promovalue");
                                    String mprice = jsonObject.getString("mprice");
                                    String description = jsonObject.getString("description");
                                    appliedpomodes = promoname;
                                    appliedpromovalue = promovalue;
                                    ShoppingFragment.appliedpromomprice = mprice;
                                    CreateDialogBox();

                                    //finish();
                                } else {
                                    CreateDialogBoxError();
                                    //error
                                }


                                //promoCodeList.add(new PromoList(promoname,promovalue,mprice,description));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("responce", error.toString());
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("total",totalprices);
                params.put("promoname", promoname);



                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    public  void CreateDialogBox() {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.promoappliedbox,
                null, false);
        final TextView couponname=(TextView)formElementsView.findViewById(R.id.couponnamebox);
        final TextView ttldiscountam=(TextView)formElementsView.findViewById(R.id.ttldiscountam);
        final TextView descdiscountam=(TextView)formElementsView.findViewById(R.id.descdiscountam);
        final  TextView okaywesometext=(TextView)formElementsView.findViewById(R.id.okaywesometext);

        couponname.setText(appliedpomodes);

        discount=((Float.valueOf(totalprice)*Float.valueOf(appliedpromovalue)/100));
        Log.e("TotalDiscount",""+discount);
        ttldiscountam.setText(discount+"");
        descdiscountam.setText("You have availed total discount of ₹"+discount);

        okaywesometext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantAlert.dismiss();
                ShoppingFragment.couponlayoutapplied.setVisibility(View.VISIBLE);
                ShoppingFragment.couponlayout.setVisibility(View.GONE);
                ShoppingFragment.appliedcoupontag.setText(appliedpomodes);
                Intent intent = new Intent();
                intent.putExtra(ShoppingFragment.DISCOUNTKEY, discount+"");
                setResult(ShoppingFragment.RESULT_CODE, intent); // You can also send result without any data using setResult(int resultCode)
                finish();

            }
        });


        quantAlert=new AlertDialog.Builder(PromoCodeActivity.this).setView(formElementsView)
                .setCancelable(false)
                .show();
        quantAlert.getWindow().getAttributes().windowAnimations = R.anim.zoom_out;
        quantAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog.dismiss();
    }

    public void CreateDialogBoxError() {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.promofailed,
                null, false);
        final TextView okay=(TextView)formElementsView.findViewById(R.id.invalidboxokaybtn);




        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantAlert.dismiss();
            }
        });


        quantAlert=new AlertDialog.Builder(PromoCodeActivity.this).setView(formElementsView)
                .setCancelable(false)
                .show();
        quantAlert.getWindow().getAttributes().windowAnimations = R.anim.zoom_out;
        quantAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog.dismiss();
    }

    public String getMyData() {
        return String.valueOf(discount);
    }




}
