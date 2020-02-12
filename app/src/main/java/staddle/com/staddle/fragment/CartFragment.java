package staddle.com.staddle.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.AddFavouriteResponse;
import staddle.com.staddle.activity.AllAddressActivity;
import staddle.com.staddle.activity.PromoCodeActivity;
import staddle.com.staddle.activity.SchedulingActivity;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.adapter.CustomSpinnerAdapter;
import staddle.com.staddle.adapter.ShoppingAdapter;
import staddle.com.staddle.bean.CurrentOrderMetaData;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.fcm.DBHelper;
import staddle.com.staddle.fcm.DBManager;
import staddle.com.staddle.paytm.module.GetCheckSum;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.RequestQueueHelper;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.setApplicationId;
import static staddle.com.staddle.activity.VendorDetailsActivityNew.CR_OR_META_DATA;

public class CartFragment extends Fragment implements AdapterView.OnItemSelectedListener {



    RecyclerView mycartlist_rec;
    DBManager dbManager ;
    ArrayList<GetVendorSubCategoryMenuListModule.MenuList> mycartlits;
    public static ShoppingAdapter shoppingAdapter;
    float total;

    //Layouts
    public static RelativeLayout Discount_applicable_lyt;
    public static RelativeLayout totalDiscountLayout;
    public static RelativeLayout couponlayout;

    public static TextView item_total_fare;
    public static TextView topay_cart;
    public static TextView Discount_applicable_txt;
    public static TextView discount_value_show;
    public static TextView price_saved_text;

    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    public static final int REQUESTCODEREFRES = 111;
    public static final int REQUEST_CODETIME = 1;

    //RequestCodes
    public static int CAT1REQCODEHOME=11111;
    public static int CAT1REQCODESALON=11110;
    public static int CAT1RESCODEHOME=2222;
    public static int CAT1RESCODESALON=3333;

    TextView headline1;
    TextView headline2;
    TextView headline3;
    public static final String DISCOUNTKEY="discount";

    //PlaceOrderComponets
    Button PlaceOrderBtn;
    private Spinner spin;
    public String SelectedPaymentMethod="";
    List<String> methods;

    public static String SELECTEDDATE = "";
    public static  String SELECTEDTIME = "";
    public static String NICKNAMESTRING="";
    public static String ADDRESSSTRING="";

    //coupon
    public static RelativeLayout couponlayoutapplied;
    public static RelativeLayout appliedminussection;
    public static RelativeLayout selectAddressShowLayout;
    public static String appliedpromovalue;
    public static String appliedpomodes;
    public static String appliedpromomprice;
    TextView couponminuspromo;
    TextView couponminusprice;
    String promoValue="";
    public static AlertDialog quantAlert;
    public static RelativeLayout mainContainerLayout;
    public static RelativeLayout txtEmptyCart;

    public static TextView appliedcoupontag;
    public static ImageView AppliedCouponCancelTag;
    public static String promotiondiscount="";


    //Address Selection
    Button AddAddressBtn;
    Button SelectAddressBtn;
    public static RelativeLayout AddressLayoutCheckout;
    public static String serviceAddress;
    public static String serviceAddressNickName;
    public static LinearLayout paychckoutlayout;
    public static TextView serviceAddressT;
    public static TextView serviceAddressNickNameT;
    public static RelativeLayout cat1salon;
    public static NestedScrollView nested_scroll_view_cart;


    View view;
    private Context mContext;
    private RecyclerView rvShopping;
    RelativeLayout cardView;
    RelativeLayout rl_no_fav;
    public static TextView  txt_no_fav,  txt_percentage, txt_overallTotalprice;
    public static TextView tv_item_total;
    Button btn_checkout;
    String userId;
    String category;


    String data;
    ApiInterface apiInterface;
    ProgressDialog pd;

    TextView txt_athome, txt_atSalon, txt_men, txt_women, txt_mens, txt_womens, txt_percentagename;
    String cidAtVendorDetails = "";
    String vid_Selected_items = "";
    String per;
    String discount = "", commision = "";
    public static  String cid;
    String vid;

    private ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myCartArrayList;

    TextView vendor_name_cart_show;

    CurrentOrderMetaData currentOrderMetaData;

    float topay;






    public CartFragment() {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        fvbi(view);
        init(view);
        dbManager.open();

        total = dbManager.getTotal();

        if(dbManager.getTotal()!=0 || dbManager.getTotal()!=0.00f){
            fetch_cart_date_from_sql();
            methods=new ArrayList<>();
            methods.add("Cash");
            methods.add("Online");
            spin = (Spinner)view.findViewById(R.id.payment_spinner);
            spin.setAdapter(new CustomSpinnerAdapter(getContext(), R.layout.spinneritemlayout, methods));
            spin.setOnItemSelectedListener(this);
            vendor_name_cart_show.setText(currentOrderMetaData.getCROR_VNAME());

             discount = currentOrderMetaData.getCRORDISCOUNT();
            Log.e("DISCOUNTX:",discount+"llll");


            if( discount.equals("")){

                totalDiscountLayout.setVisibility(View.GONE);
                Discount_applicable_txt.setVisibility(View.GONE);
                discount = "0";

            }else if(!discount.equals("0")){
                Discount_applicable_txt.setText(discount+"% off on this order.");

                totalDiscountLayout.setVisibility(View.VISIBLE);

                Log.e("DISCOUNTX:",total+"llll");
                topay= total- ((Float.valueOf(discount)/100)*total);
                item_total_fare.setText("₹ "+String.valueOf(total));
                discount_value_show.setText(" - ₹ "+(Float.valueOf(discount)/100)*total);

                topay_cart.setText("₹ "+topay);

                price_saved_text.setText("You have saved ₹ "+(Float.valueOf(discount)/100)*total +" on the bill.");

            }else{
                Discount_applicable_lyt.setVisibility(View.GONE);
            }

            txtEmptyCart.setVisibility(View.GONE);
            nested_scroll_view_cart.setVisibility(View.VISIBLE);
            paychckoutlayout.setVisibility(View.VISIBLE);
        }else{

            txtEmptyCart.setVisibility(View.VISIBLE);
            nested_scroll_view_cart.setVisibility(View.GONE);
            paychckoutlayout.setVisibility(View.GONE);

        }
        dbManager.close();




        couponlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.open();
                Intent intent=new Intent(getContext(), PromoCodeActivity.class);
                android.util.Log.e("totalll",String.valueOf(total));
                intent.putExtra("total",String.valueOf(dbManager.getTotal()));
                intent.putExtra("vid",String.valueOf(currentOrderMetaData.getCROR_VID()));
                dbManager.close();
                startActivityForResult(intent,REQUEST_CODE);
            }
        });


        PlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SelectedPaymentMethod.equals("Cash")){
                    Gson gson = new GsonBuilder().create();
                    data = gson.toJson(mycartlits);

//                    Log.e("TAG",AppPreferences.loadPreferences(getContext(),"USER_ID"));
//                    Log.e("TAG",currentOrderMetaData.getCROR_VID());
//                    Log.e("TAG",data);
//                    Log.e("TAG",String.valueOf(total));
//                    Log.e("TAG",discount );
//                    Log.e("TAG",String.valueOf(total));
//                    Log.e("TAG",String.valueOf((Float.valueOf(discount)/100.00f)*total));
//                    Log.e("TAG",appliedpomodes);
//                    Log.e("TAG",promoValue);


                    PlaceOrderOnCash(AppPreferences.loadPreferences(getContext(),"USER_ID"), currentOrderMetaData.getCROR_VID(), data, String.valueOf(total), discount ,String.valueOf((Float.valueOf(discount)/100.00f)*total), appliedpomodes,promoValue,promotiondiscount,
                            String.valueOf(total),NICKNAMESTRING,ADDRESSSTRING,SELECTEDDATE,SELECTEDTIME,"cash","-");


                }else if(SelectedPaymentMethod.equals("Online")){
                    Pay(String.valueOf(topay));
                }else {
                    Toast.makeText(getContext(),"Please Select a payment method",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentOrderMetaData.getCROR_CID()){

                    case "1":{

                        chooseHome("1");
                        break;
                    }
                    case "2":{
                        Intent intent1=new Intent(getContext(), AllAddressActivity.class);
                        intent1.putExtra("cid",cid);
                        startActivityForResult(intent1,CAT1REQCODEHOME);
                        break;
                    }
                    case "3":{
                        Intent intent1=new Intent(getContext(), AllAddressActivity.class);
                        intent1.putExtra("cid",cid);
                        startActivityForResult(intent1,CAT1REQCODEHOME);
                        break;
                    }
                    case  "4":{
                        Intent intent=new Intent(getContext(), SchedulingActivity.class);
                        intent.putExtra("atHome","1");
                        startActivityForResult(intent,CAT1REQCODESALON);
                        break;

                    }
                }
            }
        });


        AppliedCouponCancelTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponlayoutapplied.setVisibility(View.GONE);
                couponlayout.setVisibility(View.VISIBLE);
                //txt_overallTotalprice.setText(String.valueOf(topay));
                topay= total- ((Float.valueOf(discount)/100)*total);
                topay_cart.setText(" - ₹ "+topay);
                appliedminussection.setVisibility(View.GONE);
            }
        });




        return view;
    }


    private void init(View view){

        mycartlits = new ArrayList<>();
        dbManager = new DBManager(getContext());





    }
     private void fvbi(View view){
         currentOrderMetaData = getMetaData(CR_OR_META_DATA);
         mycartlist_rec = view.findViewById(R.id.mycartlist_rec);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
         linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
         mycartlist_rec.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
         item_total_fare = view.findViewById(R.id.item_total_fare);
         topay_cart = view.findViewById(R.id.topay_cart);
         Discount_applicable_txt = view.findViewById(R.id.Discount_applicable_txt);
         Discount_applicable_lyt = view.findViewById(R.id.Discount_applicable_lyt);
         totalDiscountLayout = view.findViewById(R.id.totalDiscountLayout);
         discount_value_show = view.findViewById(R.id.discount_value_show);
         price_saved_text = view.findViewById(R.id.price_saved_text);
         couponlayout = view.findViewById(R.id.couponlayout);
         vendor_name_cart_show = view.findViewById(R.id.vendor_name_cart_show);

         cardView = view.findViewById(R.id.cardView);

         txt_no_fav = view.findViewById(R.id.txt_no);
         tv_item_total = view.findViewById(R.id.tv_item_total);
         txt_percentage = view.findViewById(R.id.txt_percentage);
         txt_percentagename = view.findViewById(R.id.txt_percentagename);
         txt_overallTotalprice = view.findViewById(R.id.txt_overallTotalprice);
         appliedminussection=view.findViewById(R.id.coupon_layout_billing_sec);
         rl_no_fav = view.findViewById(R.id.rl_no);
         AddAddressBtn=view.findViewById(R.id.addaddressbtn);
         SelectAddressBtn=view.findViewById(R.id.selectaddressbtn);
         couponminusprice=view.findViewById(R.id.couponlayout_price);
         couponminuspromo=view.findViewById(R.id.couponlayout_name);
         couponlayout=view.findViewById(R.id.couponlayout);
         mainContainerLayout=view.findViewById(R.id.maincontainercart);
         txtEmptyCart = view.findViewById(R.id.emptycart);
         couponlayoutapplied=view.findViewById(R.id.couponlayoutapplied);
         appliedcoupontag=view.findViewById(R.id.AppliedTextCode);
         AppliedCouponCancelTag=view.findViewById(R.id.cancelcoupontag);
         btn_checkout = view.findViewById(R.id.btn_checkout_cart);
         AddressLayoutCheckout=view.findViewById(R.id.addresslayoutcheckout);
         headline1=view.findViewById(R.id.bookingheadline);
         paychckoutlayout=view.findViewById(R.id.paychckoutlayout);
         headline2=view.findViewById(R.id.salonnamecat1);
         headline3=view.findViewById(R.id.showdatecat1salon);
         cat1salon=view.findViewById(R.id.cat1salon);
         PlaceOrderBtn=view.findViewById(R.id.btn_placeorder);
         nested_scroll_view_cart = view.findViewById(R.id.nested_scroll_view_cart);





     }

    public   void fetch_cart_date_from_sql(){

        dbManager.open();
        Cursor cursor = dbManager.fetch();


            if (cursor.moveToFirst()){
                do{
                    String id = cursor.getString(cursor.getColumnIndex(DBHelper.ID));
                    String vid = cursor.getString(cursor.getColumnIndex(DBHelper.VID));
                    double menu_price = cursor.getDouble(cursor.getColumnIndex(DBHelper.MENU_PRICE));
                    String menu_name = cursor.getString(cursor.getColumnIndex(DBHelper.MENU_NAME));
                    String count = cursor.getString(cursor.getColumnIndex(DBHelper.COUNT));
                    double total = cursor.getDouble(cursor.getColumnIndex(DBHelper.TOTALPRICE));
                    android.util.Log.e("DBCART : ",id);
                    android.util.Log.e("DBCART : ",vid);
                    android.util.Log.e("DBCART : ",menu_price+"");
                    android.util.Log.e("DBCART : ",menu_name);
                    android.util.Log.e("DBCART : ",count);
                    android.util.Log.e("DBCART : ",total+"");
                    mycartlits.add(new GetVendorSubCategoryMenuListModule.MenuList(id,vid,menu_price,menu_name,Integer.parseInt(count),total));

                }while(cursor.moveToNext());
            }
            cursor.close();
            commision = currentOrderMetaData.getCROR_COMMISION();
            cid = currentOrderMetaData.getCROR_CID();

            total =dbManager.getTotal();

            dbManager.close();
            shoppingAdapter = new ShoppingAdapter(getContext(), mycartlits);
            mycartlist_rec.setAdapter(shoppingAdapter);
            mycartlist_rec.hasFixedSize();

            item_total_fare.setText("₹ "+String.valueOf(total));
            topay_cart.setText("₹ "+total);











    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            promotiondiscount = data.getStringExtra(DISCOUNTKEY);
            promoValue=data.getStringExtra("promovalue");
            topay_cart.setText((topay-Float.valueOf(promotiondiscount))+"");
            couponminuspromo.setText(appliedpomodes);
            couponminusprice.setText("-"+promotiondiscount);
            appliedminussection.setVisibility(View.VISIBLE);
            topay=Float.valueOf((topay-Float.valueOf(promotiondiscount)+""));
            //Toast.makeText(mContext, testResult, Toast.LENGTH_SHORT).show();
            // TODO: Do something with your extra data
        }
        if(requestCode==CAT1REQCODEHOME && resultCode==CAT1RESCODEHOME){
            NICKNAMESTRING=data.getStringExtra("nickname");
            ADDRESSSTRING=data.getStringExtra("address");
            SELECTEDTIME=data.getStringExtra("time");
            SELECTEDDATE=data.getStringExtra("date");
            //Toast.makeText(mContext, NICKNAMESTRING+ADDRESSSTRING+SELECTEDTIME+SELECTEDDATE, Toast.LENGTH_SHORT).show();
            headline1.setText("Professionals will be at ");
            headline2.setText(NICKNAMESTRING);
            headline3.setText("At "+ADDRESSSTRING);
            cat1salon.setVisibility(View.VISIBLE);
            PlaceOrderBtn.setVisibility(View.VISIBLE);


        }
        if(requestCode==CAT1REQCODESALON && resultCode==CAT1RESCODESALON){

            SELECTEDDATE=data.getStringExtra("date");
            SELECTEDTIME=data.getStringExtra("time");
            //Toast.makeText(mContext, NICKNAMESTRING+ADDRESSSTRING+SELECTEDTIME+SELECTEDDATE, Toast.LENGTH_SHORT).show();
            headline1.setText("Booking for "+mycartlits.size()+" items at ");
            headline2.setText(HomeActivity.vname);
            headline3.setText("on "+SELECTEDDATE+" ("+SELECTEDTIME+") ");
            cat1salon.setVisibility(View.VISIBLE);
            PlaceOrderBtn.setVisibility(View.VISIBLE);
        }

    }


    public void chooseHome(String cat) {

        if(cat.equals("4")){
            Intent intent=new Intent(getContext(), SchedulingActivity.class);
            intent.putExtra("atHome","1");
            startActivityForResult(intent,CAT1REQCODESALON);
        }else{
//            assert getArguments() != null;
//            String Category = getArguments().getString("Category") + ",Cancel";

            final String[] options = {"At Home","At Salon"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("Choose Service.");

            builder.setItems(options, (dialog, item) -> {
                switch (options[item]) {
                    case "At Home":
                        Intent intent1=new Intent(getContext(), AllAddressActivity.class);
                        intent1.putExtra("cid",cid);
                        startActivityForResult(intent1,CAT1REQCODEHOME);
                        //AddressLayoutCheckout.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Address required", Toast.LENGTH_SHORT).show();

                        //addressDialog(mContext);
                        break;
                    case "At Salon":
                        Intent intent=new Intent(getContext(), SchedulingActivity.class);
                        intent.putExtra("atHome","1");
                        startActivityForResult(intent,CAT1REQCODESALON);
                        //showDialogDate(mContext, serviceAddress);
                        break;
                    case "Cancel":
                        dialog.dismiss();
                        break;
                    default:
                        //Toast.makeText(getContext(), "Address required", Toast.LENGTH_SHORT).show();

                        //addressDialog(mContext);
                        break;
                }
            });
            builder.show();
        }

    }



    @SuppressLint("DefaultLocale")
    private void PlaceOrderOnCash(String userId, String vid_Selected_items, String order_list,
                                  String order_price, String discount, String discount_price,
                                  String appliedpomodes,
                                  String appliedpromovalue, String promotiondiscount,String total, String NICKNAME, String ADDRESSSTRING, String SELECTEDDATE, String SELECTEDTIME, String paymentmethod,String online_payment_id) {

//        float s = shoppingAdapter.getTotalPrice();
//
//        if (commision.equals("") || commision.equals(null)) {
//            commision = "0";
//        } else {
//            if (commision.length() > 0 && commision.charAt(commision.length() - 1) == '%') {
//                commision = commision.substring(0, commision.length() - 1);
//            }
//
//        }


        pd = new ProgressDialog(getContext());
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        android.util.Log.e("PARAMETERS",userId+":userId");
        android.util.Log.e("PARAMETERS",vid_Selected_items+":vid_Selected_items");
        android.util.Log.e("PARAMETERS",order_list+":order_list");
        android.util.Log.e("PARAMETERS",order_price+":order_price");
        android.util.Log.e("PARAMETERS",String.valueOf(discount)+":discount");
        android.util.Log.e("PARAMETERS",appliedpomodes+":appliedpomodes");
        android.util.Log.e("PARAMETERS",appliedpromovalue+":appliedpromovalue");
        android.util.Log.e("PARAMETERS",promotiondiscount+":promotiondiscount");
        android.util.Log.e("PARAMETERS",String.valueOf(discount_price)+":discount_price");
        android.util.Log.e("PARAMETERS",commision+":commision");
        android.util.Log.e("PARAMETERS",String.valueOf(topay)+":topay");
        android.util.Log.e("PARAMETERS",NICKNAME+":NICKNAME");
        android.util.Log.e("PARAMETERS",ADDRESSSTRING+":ADDRESSSTRING");
        android.util.Log.e("PARAMETERS",SELECTEDTIME+":SELECTEDTIME");
        android.util.Log.e("PARAMETERS",SELECTEDDATE+":SELECTEDDATE");
        android.util.Log.e("PARAMETERS",paymentmethod+":paymentmethod");

//        placeorderwithcashvolley(userId, vid_Selected_items,
//                order_list,String.valueOf(discountsPrice) , String.valueOf(discounts),appliedpomodes,appliedpromovalue,promotiondiscount, order_price,
//                commision, String.valueOf(totalPrice),NICKNAME,ADDRESSSTRING,SELECTEDTIME,SELECTEDDATE,paymentmethod);
        Call<AddFavouriteResponse> call = apiInterface.addOfferSave(userId, vid_Selected_items,
                order_list,String.valueOf(topay) , String.valueOf(discount),appliedpomodes,appliedpromovalue,promotiondiscount, String.valueOf(discount_price),
               String.valueOf((Float.valueOf(commision)/100)*Float.valueOf(total)), String.valueOf(total),NICKNAME,ADDRESSSTRING,SELECTEDTIME,SELECTEDDATE,paymentmethod,mycartlits.size()+"",AppPreferences.loadPreferences(getContext(),"USER_MOBILE"),AppPreferences.loadPreferences(getContext(),"USER_NAME"),online_payment_id);

        call.enqueue(new Callback<AddFavouriteResponse>() {
            @Override
            public void onResponse(Call<AddFavouriteResponse> call, final Response<AddFavouriteResponse> response) {
                pd.dismiss();
                android.util.Log.d("ORDERRESPONSE", "" + response);
                try {
                    if (response.isSuccessful()) {
                        AddFavouriteResponse responsee = response.body();
                        if (responsee != null) {
                            String message = responsee.getMessage();
                            int status = responsee.getStatus();
                            if (status == 1) {

                                mycartlits.clear();
                                DBManager db = new DBManager(getContext());
                                db.open();
                                db.truncate();
                                db.close();
                                AppPreferences.deletePref(getContext(),CR_OR_META_DATA);
                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(() -> {
                                    CreateDialogBox(VendorDetailsActivityNew.vname);

                                }, 0);
                            } else {
                                //Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddFavouriteResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    String HouseNo = "", City = "", State = "", LandMark = "", BookSlot = "", BookDate = "";

    private void Pay(String total) {
        String orderId = generateRandomNumber();



        // Live
        getChecksum("pLxUTB00403908468779", orderId, "cust123", AppPreferences.loadPreferences(getContext(),"USER_MOBILE"), "staddleservices@gmail.com",
                "WAP", total, "WEBSTAGING", "Retail", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
    }



    public String generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder s = new StringBuilder("OR");
        for (int i = 0; i < 4; i++) {
            int number = secureRandom.nextInt(9);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zero
                i = -1;
                continue;
            }
            s.append(number);
        }
        return s.toString();
    }

    public void getChecksum(String mid, String orderId, String custId, String mobile, String email,
                            String wap, String total, String webstaging, String industry_type_id, String callback_url) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, "Please wait...", false, false);
        progressDialog.setCancelable(false);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetCheckSum> call = apiInterface.getChecksum(mid, orderId, custId, mobile, email,
                wap, total, webstaging, industry_type_id, callback_url);
        call.enqueue(new Callback<GetCheckSum>() {
            @Override
            public void onResponse(@NonNull Call<GetCheckSum> call, @NonNull Response<GetCheckSum> response) {
                progressDialog.dismiss();
                com.paytm.pgsdk.Log.e("Response:", "Response:" + response.message());
                try {
                    assert response.body() != null;
                    String CHECKSUMHASH = response.body().CHECKSUMHASH;
                    String ORDER_ID = response.body().ORDER_ID;
                    String payt_STATUS = response.body().payt_STATUS;
                    verifyChecksum(mid, orderId, custId, mobile, email,
                            wap, total, webstaging, industry_type_id, callback_url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCheckSum> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                com.paytm.pgsdk.Log.e("Throwable:", "Throwable:" + t.toString());
            }
        });
    }

    public void verifyChecksum(String mid, String orderId, String custId, String mobile, String email,
                               String wap, String total, String webstaging, String industry_type_id, String callback_url) {

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, "Please wait...", false, false);
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://staddle.in/mobileapp/api/verifyChecksum.php?",
                response -> {
                    progressDialog.dismiss();
                    try {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("MID", mid);
                        paramMap.put("ORDER_ID", orderId);
                        paramMap.put("CUST_ID", custId);
                        paramMap.put("MOBILE_NO", mobile);
                        paramMap.put("EMAIL", email);
                        paramMap.put("CHANNEL_ID", wap);
                        paramMap.put("TXN_AMOUNT", total);
                        paramMap.put("WEBSITE", webstaging);
                        paramMap.put("INDUSTRY_TYPE_ID", industry_type_id);
                        paramMap.put("CALLBACK_URL", callback_url);
                        paramMap.put("CHECKSUMHASH", response);
                        placeOrder(paramMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
            progressDialog.dismiss();
            if (error instanceof ServerError) {
                Toast.makeText(getActivity(), "Check your Server Side ", Toast.LENGTH_SHORT).show();
                //TODO
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("MID", mid);
                params.put("ORDER_ID", orderId);
                params.put("CUST_ID", custId);
                params.put("MOBILE_NO", mobile);
                params.put("EMAIL", email);
                params.put("CHANNEL_ID", wap);
                params.put("TXN_AMOUNT", total);
                params.put("WEBSITE", webstaging);
                params.put("INDUSTRY_TYPE_ID", industry_type_id);
                params.put("CALLBACK_URL", callback_url);
                return params;
            }
        };
        RequestQueueHelper.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void placeOrder(Map<String, String> params) {
        // choosing between PayTM staging and production
//        PaytmPGService pgService = PaytmPGService.getStagingService();
        PaytmPGService pgService = PaytmPGService.getProductionService();
        String online_payment_id = params.get("ORDER_ID");

        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) params);
        pgService.initialize(Order, null);
        pgService.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getActivity(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onTransactionResponse(Bundle inResponse) {
                String message = inResponse.getString("RESPMSG");
                addOrder("Payment Transaction response " + inResponse.getString("RESPMSG"),online_payment_id);
            }

            public void networkNotAvailable() {
                Toast.makeText(getActivity(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(getActivity(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getActivity(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
            }

            public void onBackPressedCancelTransaction() {
                Toast.makeText(getActivity(), "Transaction cancelled", Toast.LENGTH_LONG).show();
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(getActivity(), "Transaction Cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @SuppressLint("NewApi")
    public void addOrder(String message, String online_payment_id) {

        if (message.equalsIgnoreCase("Payment Transaction response Txn Success"))
            PlaceOrderOnCash(AppPreferences.loadPreferences(getContext(),"USER_ID"), currentOrderMetaData.getCROR_VID(), data, String.valueOf(total), discount ,String.valueOf((Float.valueOf(discount)/100)*total), appliedpomodes,promoValue,promotiondiscount,
                    String.valueOf(total),NICKNAMESTRING,ADDRESSSTRING,SELECTEDDATE,SELECTEDTIME,"cash","-");
        else
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

    }


//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        SelectedPaymentMethod = String.valueOf(adapterView.getItemAtPosition(i));
//        Toast.makeText(getContext(),SelectedPaymentMethod , Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }


    public  void CreateDialogBox(String vendorname) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.placeorderconfirmtion,
                null, false);
        final  TextView vendornametxt=(TextView)formElementsView.findViewById(R.id.tag2orderplaced);
        final  TextView okaywesometext=(TextView)formElementsView.findViewById(R.id.okaywesometextorderplaced);
        vendornametxt.setText("You will get notified when "+vendorname+" confirm your order");
        okaywesometext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        quantAlert=new AlertDialog.Builder(getContext()).setView(formElementsView)
                .setCancelable(false)
                .show();
        quantAlert.getWindow().getAttributes().windowAnimations = R.anim.zoom_out;
        quantAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //progressDialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SelectedPaymentMethod = String.valueOf(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public CurrentOrderMetaData getMetaData(String key){

        Gson gson = new Gson();
        String json = AppPreferences.loadPreferences(getContext(),key);
        return  gson.fromJson(json, CurrentOrderMetaData.class);
    }


}
