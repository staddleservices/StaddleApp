
package staddle.com.staddle.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.AddFavouriteResponse;
import staddle.com.staddle.activity.PromoCodeActivity;
import staddle.com.staddle.activity.SelectDeliveryAddress;
import staddle.com.staddle.adapter.ShoppingAdapter;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.paytm.module.GetCheckSum;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;
import staddle.com.staddle.utils.Alerts;
import staddle.com.staddle.utils.CheckNetwork;
import staddle.com.staddle.utils.RequestQueueHelper;

import static android.graphics.Color.TRANSPARENT;
import static staddle.com.staddle.HomeActivity.ttldiscount;

public class ShoppingFragment extends Fragment {
    View view;
    private Context mContext;
    private RecyclerView rvShopping;
    CardView cardView;
    RelativeLayout rl_no_fav;
    public static TextView txtNoMeassage, txt_no_fav,  txt_percentage, txt_overallTotalprice;
    public static TextView tv_item_total;
    Button btn_checkout;
    String userId;
    private ShoppingAdapter shoppingAdapter;

    private ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myCartArrayList;

    String hourss = "", minn = "", slottime = "", minuteCurrent = "", currentdate = "", currentMonth = "", currentYear = "";
    TextView txt_slot1;
    int hoursn, dayOfMonthSelected;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String offer_start_date = "", offer_booking_time = "";

    String data, totalp, total;
    ApiInterface apiInterface;
    ProgressDialog pd;

    TextView txt_athome, txt_atSalon, txt_men, txt_women, txt_mens, txt_womens, txt_percentagename;
    String cidAtVendorDetails = "";
    String vid_Selected_items = "";
    String per;
    String discount = "", commision = "";
    String cid="";
    String vid;

    //coupon
    public static RelativeLayout couponlayout;
    public static RelativeLayout couponlayoutapplied;
    public static LinearLayout appliedminussection;
    public static String appliedpromovalue;
    public static String appliedpomodes;
    public static String appliedpromomprice;
    TextView couponminuspromo;
    TextView couponminusprice;

    public static TextView appliedcoupontag;
    public static ImageView AppliedCouponCancelTag;
    public static String promotiondiscount="";
    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    public static final String DISCOUNTKEY="discount";


    public ShoppingFragment() {

    }

    public static ShoppingFragment newInstance(Bundle bundle) {
        ShoppingFragment fragment = new ShoppingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            promotiondiscount = data.getStringExtra(DISCOUNTKEY);
            txt_overallTotalprice.setText((Float.valueOf(totalp)-Float.valueOf(promotiondiscount))+"");
            couponminuspromo.setText(appliedpomodes);
            couponminusprice.setText("-"+promotiondiscount);
            appliedminussection.setVisibility(View.VISIBLE);
            totalp=(Float.valueOf(totalp)-Float.valueOf(promotiondiscount)+"");
            //Toast.makeText(mContext, testResult, Toast.LENGTH_SHORT).show();
            // TODO: Do something with your extra data
        }
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shopping, container, false);
        mContext = getActivity();
        assert mContext != null;
        userId = AppPreferences.loadPreferences(mContext, "USER_ID");

        Bundle bundle = getArguments();
         cid=bundle.getString("CID");
         vid=bundle.getString("vid");
         Log.e("VID",vid);


        find_All_IDs(view);

        couponlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), PromoCodeActivity.class);
                intent.putExtra("total",total);
                intent.putExtra("vid",vid);
                startActivityForResult(intent,REQUEST_CODE);

            }
        });

        AppliedCouponCancelTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponlayoutapplied.setVisibility(View.GONE);
                couponlayout.setVisibility(View.VISIBLE);
                txt_overallTotalprice.setText(totalp);
                appliedminussection.setVisibility(View.GONE);
            }
        });

        myCartArrayList = new ArrayList<>();
        myCartArrayList = HomeActivity.myCartArrayList;

        discount = HomeActivity.discount;
        commision = HomeActivity.commision;
        txt_percentagename.setText(discount + " OFF");
        for (int i = 0; i < myCartArrayList.size(); i++) {
            vid_Selected_items = myCartArrayList.get(i).getVid();
        }

        shoppingAdapter = new ShoppingAdapter(mContext, myCartArrayList);
        rvShopping.setAdapter(shoppingAdapter);
        shoppingAdapter.notifyDataSetChanged();

        setListner();

        int s = shoppingAdapter.getTotalPrice();

        totalp = Integer.valueOf(s).toString();

        if (totalp.equals("0")) {
            cardView.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.GONE);
            txtNoMeassage.setVisibility(View.VISIBLE);
        } else {
            cardView.setVisibility(View.VISIBLE);
            btn_checkout.setVisibility(View.VISIBLE);
            txtNoMeassage.setVisibility(View.GONE);
        }

        try {
            if (discount.equals("")) {
                discount = "0";
            } else {
                if (discount.length() > 0 && discount.charAt(discount.length() - 1) == '%') {
                    discount = discount.substring(0, discount.length() - 1);
                }
                int discount1 = Integer.parseInt(discount);
                double res = ((s / 100.0f) * discount1);
                if (res < 500) {
                    per = String.valueOf(res);
                    float f = Float.parseFloat(per);

                    txt_percentage.setText(String.format("%.02f", f));
                    double ttt = s - res;
                    total = String.valueOf(ttt);
                    if(promotiondiscount==""){
                        float f1 = Float.parseFloat(total);
                        txt_overallTotalprice.setText(String.format("%.02f", f1));
                    }else{
                        float f1 = Float.valueOf(shoppingAdapter.getTotalPrice());
                        float newdes=(f1-(f1*Float.valueOf(appliedpromovalue)/100));
                        txt_overallTotalprice.setText(String.format("%.02f", newdes));
                    }
                } else {
                    per = String.valueOf(500);
                    float f = Float.parseFloat(per);
                    txt_percentage.setText(String.format("%.02f", f));
                    double ttt = s - 500;
                    total = String.valueOf(ttt);
                    if(promotiondiscount==""){
                        float f1 = Float.parseFloat(total);
                        txt_overallTotalprice.setText(String.format("%.02f", f1));
                    }else{
                        float f1 = Float.valueOf(shoppingAdapter.getTotalPrice());
                        float newdes=(f1-(f1*Float.valueOf(appliedpromovalue)/100));
                        txt_overallTotalprice.setText(String.format("%.02f", newdes));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        shoppingAdapter.setCPlusButtonClickListener((position, menuList) -> {

            if(promotiondiscount==""){
                int s1 = shoppingAdapter.getTotalPrice();
                totalp = Integer.valueOf(s1).toString();
                int discount1 = Integer.parseInt(discount);

                float res =  ((s1 / 100.0f) * discount1);
                if (res < 500) {
                    float ttt = s1 - res;
                    per = String.valueOf(res);
                    float f = Float.parseFloat(per);
                    txt_percentage.setText(String.format("%.02f", f));
                    total = String.valueOf(ttt);

                    if(promotiondiscount==""){
                        float f1 = Float.parseFloat(total);
                        txt_overallTotalprice.setText(String.format("%.02f", f1));
                    }else{
                        float f1 = Float.valueOf(shoppingAdapter.getTotalPrice());
                        float newdes=(f1-(f1*Float.valueOf(appliedpromovalue)/100));
                        //float dsf=f1-newdes;
                        txt_overallTotalprice.setText(String.format("%.02f", newdes));
                    }

                    tv_item_total.setText(totalp);
                } else {
                    float ttt = s1 - 500;
                    per = String.valueOf(500);
                    float f = Float.parseFloat(per);
                    txt_percentage.setText(String.format("%.02f", f));
                    total = String.valueOf(ttt);
                    if(promotiondiscount==""){
                        float f1 = Float.parseFloat(total);
                        txt_overallTotalprice.setText(String.format("%.02f", f1));
                    }else{
                        float f1 = Float.valueOf(shoppingAdapter.getTotalPrice());
                        float newdes=(f1-(f1*Float.valueOf(appliedpromovalue)/100));
                        //float dsf=f1-newdes;
                        txt_overallTotalprice.setText(String.format("%.02f", newdes));
                    }
                    tv_item_total.setText(totalp);
                }
            }else{

                couponlayoutapplied.setVisibility(View.GONE);
                couponlayout.setVisibility(View.VISIBLE);
                txt_overallTotalprice.setText(Float.valueOf(shoppingAdapter.getTotalPrice())+"");
                totalp=Float.valueOf(shoppingAdapter.getTotalPrice())+"";
                tv_item_total.setText(totalp);
                appliedminussection.setVisibility(View.GONE);
                promotiondiscount="";
            }

        });

        tv_item_total.setText(totalp);
        Gson gson = new GsonBuilder().create();
        data = gson.toJson(myCartArrayList);

        cidAtVendorDetails = AppPreferences.loadPreferences(mContext, "cidAtVendorDetails");

        assert getArguments() != null;
        String tag = getArguments().getString("Tag");

        btn_checkout.setOnClickListener(view -> {

            Intent intent=new Intent(mContext, SelectDeliveryAddress.class);
            startActivity(intent);
//            if(cid.equals("4")){
//                showDialogDate(mContext, "", "", "", "");
//
//            }
//            else if (tag != null && !tag.equals("")) {
//                if (tag.equals("HomeVender"))
//                    chooseHome();
//                else
//                    addressDialog(mContext);
//            }
//            else {
//                showDialogDate(mContext, "", "", "", "");
//            }
        });

        return view;

    }

    public void chooseHome() {
        assert getArguments() != null;
        String Category = getArguments().getString("Category") + ",Cancel";
        final String[] options = Category.split(",");
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle("Choose Service.");

        builder.setItems(options, (dialog, item) -> {
            switch (options[item]) {
                case "At Home":
                    addressDialog(mContext);
                    break;
                case "At Salon":
                    showDialogDate(mContext, "", "", "", "");
                    break;
                case "Cancel":
                    dialog.dismiss();
                    break;
                default:
                    addressDialog(mContext);
                    break;
            }
        });
        builder.show();
    }

    @SuppressLint("DefaultLocale")
    public void setListner() {
        shoppingAdapter.setOnItemClickListener((subcategoryTreeListModel, position) -> {
            try {
                if(promotiondiscount==""){
                    myCartArrayList.remove(position);
                    rvShopping.setAdapter(shoppingAdapter);
                    shoppingAdapter.notifyDataSetChanged();

                    int s1 = shoppingAdapter.getTotalPrice();
                    totalp = Integer.valueOf(s1).toString();
                    int discount1 = Integer.parseInt(discount);

                    int res = (int) ((s1 / 100.2f) * discount1);

                    int ttt = s1 - res;
                    per = String.valueOf(res);

                    float f = Float.parseFloat(per);
                    txt_percentage.setText(String.format("%.02f", f));

                    total = String.valueOf(ttt);
                    float f1 = Float.parseFloat(total);
                    txt_overallTotalprice.setText(String.format("%.02f", f1));
                    tv_item_total.setText(totalp);

                    if (totalp.equals("0")) {
                        cardView.setVisibility(View.GONE);
                        btn_checkout.setVisibility(View.GONE);
                        txtNoMeassage.setVisibility(View.VISIBLE);
                    } else {
                        cardView.setVisibility(View.VISIBLE);
                        btn_checkout.setVisibility(View.VISIBLE);
                        txtNoMeassage.setVisibility(View.GONE);
                    }
                }else{
                    couponlayoutapplied.setVisibility(View.GONE);
                    couponlayout.setVisibility(View.VISIBLE);
                    txt_overallTotalprice.setText(Float.valueOf(shoppingAdapter.getTotalPrice())+"");
                    totalp=Float.valueOf(shoppingAdapter.getTotalPrice())+"";
                    tv_item_total.setText(totalp);
                    appliedminussection.setVisibility(View.GONE);
                    promotiondiscount="";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void atSalonDialog(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = mInflater.inflate(R.layout.dialog_select_type_athome, null);
        builder1.setView(dialogView);
        builder1.setCancelable(true);

        final AlertDialog alertDialog = builder1.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);

        txt_athome = dialogView.findViewById(R.id.txt_athome);
        txt_atSalon = dialogView.findViewById(R.id.txt_atSalon);
        txt_men = dialogView.findViewById(R.id.txt_men);
        txt_women = dialogView.findViewById(R.id.txt_women);
        txt_mens = dialogView.findViewById(R.id.txt_mens);
        txt_womens = dialogView.findViewById(R.id.txt_womens);

        txt_men.setOnClickListener(view -> {
            AppPreferences.savePreferences(mContext, "AtHomeMenDialog", "1");
//            showDialogDate(mContext);
            alertDialog.dismiss();
        });
        txt_women.setOnClickListener(view -> {
            AppPreferences.savePreferences(mContext, "AtHomeWomenDialog", "2");
//            showDialogDate(mContext);
            alertDialog.dismiss();
        });
        txt_mens.setOnClickListener(view -> {
            AppPreferences.savePreferences(mContext, "AtSalonMenDialog", "3");
//            selectOption();
            alertDialog.dismiss();
        });
        txt_womens.setOnClickListener(view -> {
            AppPreferences.savePreferences(mContext, "AtSalonWomenDialog", "4");
//            selectOption();
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    public void showDialogDate(Context context, String house_number, String city, String state, String landmark) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = mInflater.inflate(R.layout.dialog_swipe, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.show();

        TextView txt_cancel = (TextView) dialogView.findViewById(R.id.txt_cancel);
        TextView txt_conform = (TextView) dialogView.findViewById(R.id.txt_conform);

        Button btnTime = dialogView.findViewById(R.id.btnTime);
        Button btn_date = dialogView.findViewById(R.id.btn_date);
        TextView txtDate = dialogView.findViewById(R.id.txtDate);
        TextView txt_time = dialogView.findViewById(R.id.txt_time);

        LinearLayout ll_AllSlots = dialogView.findViewById(R.id.ll_AllSlots);
        TextView rl_NoSlots = dialogView.findViewById(R.id.rl_NoSlots);

        //for slot
        txt_slot1 = dialogView.findViewById(R.id.txt_slot1);
        TextView txt_slot2 = dialogView.findViewById(R.id.txt_slot2);
        TextView txt_slot3 = dialogView.findViewById(R.id.txt_slot3);
        TextView txt_slot4 = dialogView.findViewById(R.id.txt_slot4);
        TextView txt_slot5 = dialogView.findViewById(R.id.txt_slot5);
        TextView txt_slot6 = dialogView.findViewById(R.id.txt_slot6);
        TextView txt_slot7 = dialogView.findViewById(R.id.txt_slot7);
        TextView txt_slot8 = dialogView.findViewById(R.id.txt_slot8);

        txt_slot1.setClickable(false);
        txt_slot2.setClickable(false);
        txt_slot3.setClickable(false);
        txt_slot4.setClickable(false);
        txt_slot5.setClickable(false);
        txt_slot6.setClickable(false);
        txt_slot7.setClickable(false);

        final Calendar c = Calendar.getInstance();
        currentdate = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        currentYear = String.valueOf(c.get(Calendar.YEAR));
        currentMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
        String allCurrentDate = currentdate + "-" + currentMonth + "-" + currentYear;
        int mhourCurrent = c.get(Calendar.HOUR_OF_DAY);
        minuteCurrent = String.valueOf(c.get(Calendar.MINUTE));

        txtDate.setText(currentdate + "-" + currentMonth + "-" + currentYear);
        txt_time.setText(mhourCurrent + ":" + minuteCurrent);


        if (txtDate.getText().toString().equalsIgnoreCase(allCurrentDate)) {
            if (mhourCurrent >= 8 && mhourCurrent <= 10) {
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                txt_slot1.setEnabled(false);

                txt_slot2.setEnabled(true);
                txt_slot3.setEnabled(true);
                txt_slot4.setEnabled(true);
                txt_slot5.setEnabled(true);
                txt_slot6.setEnabled(true);
                txt_slot7.setEnabled(true);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 10 && mhourCurrent <= 12) {
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                txt_slot2.setEnabled(false);
                txt_slot1.setEnabled(false);

                txt_slot3.setEnabled(true);
                txt_slot4.setEnabled(true);
                txt_slot5.setEnabled(true);
                txt_slot6.setEnabled(true);
                txt_slot7.setEnabled(true);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 12 && mhourCurrent <= 14) {
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                txt_slot3.setEnabled(false);
                txt_slot1.setEnabled(false);
                txt_slot2.setEnabled(false);

                txt_slot4.setEnabled(true);
                txt_slot5.setEnabled(true);
                txt_slot6.setEnabled(true);
                txt_slot7.setEnabled(true);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 14 && mhourCurrent <= 16) {
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                txt_slot4.setEnabled(false);
                txt_slot1.setEnabled(false);
                txt_slot2.setEnabled(false);
                txt_slot3.setEnabled(false);

                txt_slot5.setEnabled(true);
                txt_slot6.setEnabled(true);
                txt_slot7.setEnabled(true);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 16 && mhourCurrent <= 18) {
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                txt_slot5.setEnabled(false);
                txt_slot4.setEnabled(false);
                txt_slot3.setEnabled(false);
                txt_slot2.setEnabled(false);
                txt_slot1.setEnabled(false);

                txt_slot6.setEnabled(true);
                txt_slot7.setEnabled(true);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 18 && mhourCurrent <= 20) {
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                txt_slot6.setEnabled(false);
                txt_slot5.setEnabled(false);
                txt_slot4.setEnabled(false);
                txt_slot3.setEnabled(false);
                txt_slot2.setEnabled(false);
                txt_slot1.setEnabled(false);

                txt_slot7.setEnabled(true);
                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 20 && mhourCurrent <= 22) {
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot7.setEnabled(false);
                txt_slot6.setEnabled(false);
                txt_slot5.setEnabled(false);
                txt_slot4.setEnabled(false);
                txt_slot3.setEnabled(false);
                txt_slot2.setEnabled(false);
                txt_slot1.setEnabled(false);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

            } else if (mhourCurrent >= 22 && mhourCurrent <= 24) {
                txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                txt_slot7.setEnabled(false);
                txt_slot6.setEnabled(false);
                txt_slot5.setEnabled(false);
                txt_slot4.setEnabled(false);
                txt_slot3.setEnabled(false);
                txt_slot2.setEnabled(false);
                txt_slot1.setEnabled(false);
                txt_slot8.setEnabled(false);

                ll_AllSlots.setVisibility(View.VISIBLE);
                rl_NoSlots.setVisibility(View.GONE);

                Toast.makeText(mContext, "sorry shop is closed ", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "vmfdbfdfd d m d", Toast.LENGTH_LONG).show();

        }


        btn_date.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c1 = Calendar.getInstance();
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            Date What_Is_Today = Calendar.getInstance().getTime();

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String monthofyear = String.valueOf(view.getMonth());
                            String dayofmonthString = String.valueOf(view.getDayOfMonth());
                            dayOfMonthSelected = view.getDayOfMonth();

                            if (mDay == dayOfMonth && monthOfYear == mMonth) {
                                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                                btnTime.setVisibility(View.VISIBLE);
                                ll_AllSlots.setVisibility(View.GONE);
                                rl_NoSlots.setVisibility(View.VISIBLE);

                            } else if (dayOfMonth > mDay) {
                                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                                txt_slot1.setEnabled(true);
                                txt_slot2.setEnabled(true);
                                txt_slot3.setEnabled(true);
                                txt_slot4.setEnabled(true);
                                txt_slot5.setEnabled(true);
                                txt_slot6.setEnabled(true);
                                txt_slot7.setEnabled(true);
                                txt_slot8.setEnabled(true);

                                btnTime.setVisibility(View.GONE);
                                ll_AllSlots.setVisibility(View.VISIBLE);
                                rl_NoSlots.setVisibility(View.GONE);

                            } else if (monthOfYear > mMonth) {
                                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                                txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                                txt_slot1.setEnabled(true);
                                txt_slot2.setEnabled(true);
                                txt_slot3.setEnabled(true);
                                txt_slot4.setEnabled(true);
                                txt_slot5.setEnabled(true);
                                txt_slot6.setEnabled(true);
                                txt_slot7.setEnabled(true);
                                txt_slot8.setEnabled(true);

                                btnTime.setVisibility(View.GONE);

                                ll_AllSlots.setVisibility(View.VISIBLE);
                                rl_NoSlots.setVisibility(View.GONE);

                            } else if (dayOfMonth < mDay) {
                                txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                                txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                                txt_slot1.setEnabled(false);
                                txt_slot2.setEnabled(false);
                                txt_slot3.setEnabled(false);
                                txt_slot4.setEnabled(false);
                                txt_slot5.setEnabled(false);
                                txt_slot6.setEnabled(false);
                                txt_slot7.setEnabled(false);
                                txt_slot8.setEnabled(false);

                                btnTime.setVisibility(View.GONE);
                                ll_AllSlots.setVisibility(View.VISIBLE);
                                rl_NoSlots.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(mContext, " all available", Toast.LENGTH_LONG).show();
                            }
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            offer_start_date = txtDate.getText().toString().trim();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        btnTime.setOnClickListener(view -> {
            // Get Current Time
            final Calendar c12 = Calendar.getInstance();
            mHour = c12.get(Calendar.HOUR_OF_DAY);
            mMinute = c12.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hourss = String.valueOf(view.getHour());
                    hoursn = view.getHour();
                    minn = String.valueOf(view.getMinute());

                    if (mDay == dayOfMonthSelected) {
                        if (hoursn >= 8 && hoursn <= 10) {
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                            txt_slot1.setEnabled(false);

                            txt_slot2.setEnabled(true);
                            txt_slot3.setEnabled(true);
                            txt_slot4.setEnabled(true);
                            txt_slot5.setEnabled(true);
                            txt_slot6.setEnabled(true);
                            txt_slot7.setEnabled(true);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                        } else if (hoursn >= 10 && hoursn <= 12) {
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                            txt_slot2.setEnabled(false);
                            txt_slot1.setEnabled(false);

                            txt_slot3.setEnabled(true);
                            txt_slot4.setEnabled(true);
                            txt_slot5.setEnabled(true);
                            txt_slot6.setEnabled(true);
                            txt_slot7.setEnabled(true);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                        } else if (hoursn >= 12 && hoursn <= 14) {
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                            txt_slot3.setEnabled(false);
                            txt_slot1.setEnabled(false);
                            txt_slot2.setEnabled(false);

                            txt_slot4.setEnabled(true);
                            txt_slot5.setEnabled(true);
                            txt_slot6.setEnabled(true);
                            txt_slot7.setEnabled(true);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                        } else if (hoursn >= 14 && hoursn <= 16) {
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                            txt_slot4.setEnabled(false);
                            txt_slot1.setEnabled(false);
                            txt_slot2.setEnabled(false);
                            txt_slot3.setEnabled(false);

                            txt_slot5.setEnabled(true);
                            txt_slot6.setEnabled(true);
                            txt_slot7.setEnabled(true);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                        } else if (hoursn >= 16 && hoursn <= 18) {
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                            txt_slot5.setEnabled(false);
                            txt_slot4.setEnabled(false);
                            txt_slot3.setEnabled(false);
                            txt_slot2.setEnabled(false);
                            txt_slot1.setEnabled(false);

                            txt_slot6.setEnabled(true);
                            txt_slot7.setEnabled(true);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);


                        } else if (hoursn >= 18 && hoursn <= 20) {
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));

                            txt_slot6.setEnabled(false);
                            txt_slot5.setEnabled(false);
                            txt_slot4.setEnabled(false);
                            txt_slot3.setEnabled(false);
                            txt_slot2.setEnabled(false);
                            txt_slot1.setEnabled(false);

                            txt_slot7.setEnabled(true);
                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                        } else if (hoursn >= 20 && hoursn <= 22) {
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot7.setEnabled(false);
                            txt_slot6.setEnabled(false);
                            txt_slot5.setEnabled(false);
                            txt_slot4.setEnabled(false);
                            txt_slot3.setEnabled(false);
                            txt_slot2.setEnabled(false);
                            txt_slot1.setEnabled(false);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                        } else if (hoursn >= 22 && hoursn <= 24) {
                            txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot7.setEnabled(false);
                            txt_slot6.setEnabled(false);
                            txt_slot5.setEnabled(false);
                            txt_slot4.setEnabled(false);
                            txt_slot3.setEnabled(false);
                            txt_slot2.setEnabled(false);
                            txt_slot1.setEnabled(false);
                            txt_slot8.setEnabled(false);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                            Toast.makeText(mContext, "sorry shop is closed ", Toast.LENGTH_LONG).show();

                        } else {
                            txt_slot8.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot6.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));
                            txt_slot7.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

                            txt_slot7.setEnabled(false);
                            txt_slot6.setEnabled(false);
                            txt_slot5.setEnabled(false);
                            txt_slot4.setEnabled(false);
                            txt_slot3.setEnabled(false);
                            txt_slot2.setEnabled(false);
                            txt_slot1.setEnabled(false);
                            txt_slot8.setEnabled(false);

                            ll_AllSlots.setVisibility(View.VISIBLE);
                            rl_NoSlots.setVisibility(View.GONE);

                            Toast.makeText(mContext, "slots not available", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Please enter date", Toast.LENGTH_LONG).show();
                    }

                    txt_time.setText(hourss + ":" + minn);
                    offer_booking_time = txt_time.getText().toString().trim();
                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        });

        txt_slot1.setOnClickListener(view -> {
            slottime = txt_slot1.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot2.setOnClickListener(view -> {

            slottime = txt_slot2.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot3.setOnClickListener(view -> {

            slottime = txt_slot3.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot4.setOnClickListener(view -> {

            slottime = txt_slot4.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot5.setOnClickListener(view -> {

            slottime = txt_slot5.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot6.setOnClickListener(view -> {

            slottime = txt_slot6.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot7.setOnClickListener(view -> {
            slottime = txt_slot7.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_slot8.setOnClickListener(view -> {
            slottime = txt_slot8.getText().toString();
            txt_time.setText(slottime);
            offer_booking_time = txt_time.getText().toString().trim();
        });

        txt_cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });



        txt_conform.setOnClickListener(view -> {
            offer_start_date = txtDate.getText().toString().trim();
            slottime = txt_time.getText().toString().trim();
            if (!offer_start_date.equalsIgnoreCase("") && !slottime.equalsIgnoreCase("")) {
                if (CheckNetwork.isNetworkAvailable(mContext)) {
                    alertDialog.dismiss();
                    selectOption(house_number, city, state, landmark, slottime, offer_start_date);
                } else {
                    Alerts.showAlert(mContext);
                }
            } else {
                Toast.makeText(mContext, "Please add Date and Time Slots..", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void find_All_IDs(View view) {
        cardView = view.findViewById(R.id.cardView);
        rvShopping = view.findViewById(R.id.rvShopping);
        txtNoMeassage = view.findViewById(R.id.txtNoMeassage);
        txt_no_fav = view.findViewById(R.id.txt_no);
        tv_item_total = view.findViewById(R.id.tv_item_total);
        txt_percentage = view.findViewById(R.id.txt_percentage);
        txt_percentagename = view.findViewById(R.id.txt_percentagename);
        txt_overallTotalprice = view.findViewById(R.id.txt_overallTotalprice);
        appliedminussection=view.findViewById(R.id.appliedminustexts);
        rl_no_fav = view.findViewById(R.id.rl_no);
        couponminusprice=view.findViewById(R.id.discountminusprice);
        couponminuspromo=view.findViewById(R.id.couponminustext);
        couponlayout=view.findViewById(R.id.couponlayout);
        couponlayoutapplied=view.findViewById(R.id.couponlayoutapplied);
        appliedcoupontag=view.findViewById(R.id.AppliedTextCode);
        AppliedCouponCancelTag=view.findViewById(R.id.cancelcoupontag);
        btn_checkout = view.findViewById(R.id.btn_checkout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvShopping.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
    }

    private void selectOption(String house_number, String city, String state, String landmark, String booking_slot, String booked_date) {

        final CharSequence[] options = {"Cash", "Online", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle("Add Type!");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Cash")) {
                Gson gson = new GsonBuilder().create();
                data = gson.toJson(myCartArrayList);

                addOfferSave(userId, vid_Selected_items, data, totalp, discount, per,
                        total, house_number, city, state, landmark, booking_slot, booked_date, "cash");
            } else if (options[item].equals("Online")) {
                Gson gson = new GsonBuilder().create();
                data = gson.toJson(myCartArrayList);
                dialog.dismiss();
                HouseNo = house_number;
                City = city;
                State = state;
                LandMark = landmark;
                BookSlot = booking_slot;
                BookDate = booked_date;
                Pay(total);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void addressDialog(Context context) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.address_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        alertDialog.show();
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        EditText edtLocation = dialogView.findViewById(R.id.edtLocation);
        EditText edtHomeNo = dialogView.findViewById(R.id.edtHomeNo);
//        EditText edtAddress = dialogView.findViewById(R.id.edtAddress);
        EditText edtPincode = dialogView.findViewById(R.id.edtPincode);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        EditText edtLandMark = dialogView.findViewById(R.id.edtLandMark);
        TextView btn_not_now = dialogView.findViewById(R.id.btn_not_now);
        TextView btn_yes = dialogView.findViewById(R.id.btn_yes);

        btn_not_now.setOnClickListener(view -> alertDialog.dismiss());

        btn_yes.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edtLocation.getText().toString().trim())) {
                edtLocation.setError(getString(R.string.error_empty_fileds));
                edtLocation.requestFocus();
            } else if (edtLocation.getText().toString().trim().length() < 3) {
                edtLocation.setError(getString(R.string.invalid_location));
                edtLocation.requestFocus();
            } else if (TextUtils.isEmpty(edtHomeNo.getText().toString().trim())) {
                edtHomeNo.setError(getString(R.string.error_empty_fileds));
                edtHomeNo.requestFocus();
            } else if (edtHomeNo.getText().toString().trim().length() < 3) {
                edtHomeNo.setError(getString(R.string.invalid_home_no));
                edtHomeNo.requestFocus();
            } else if (TextUtils.isEmpty(edtLandMark.getText().toString().trim())) {
                edtLandMark.setError(getString(R.string.error_empty_fileds));
                edtLandMark.requestFocus();
            } else if (edtLandMark.getText().toString().trim().length() < 3) {
                edtLandMark.setError(getString(R.string.invalid_landmark));
                edtLandMark.requestFocus();
            } else if (TextUtils.isEmpty(edtPincode.getText().toString().trim())) {
                edtPincode.setError(getString(R.string.error_empty_fileds));
                edtPincode.requestFocus();
            } else if (edtPincode.getText().toString().trim().length() < 6) {
                edtPincode.setError(getString(R.string.invalid_pincode));
                edtPincode.requestFocus();
            } else if (TextUtils.isEmpty(edtPhone.getText().toString().trim())) {
                edtPhone.setError(getString(R.string.error_empty_fileds));
                edtPhone.requestFocus();
            } else if (edtPhone.getText().toString().trim().length() < 10) {
                edtPhone.setError(getString(R.string.invalid_phone));
                edtPhone.requestFocus();
            } else {
                alertDialog.dismiss();
                showDialogDate(mContext, edtLocation.getText().toString().trim() + " " + edtHomeNo.getText().toString().trim(), edtPincode.getText().toString().trim()
                        , edtPhone.getText().toString().trim(), edtLandMark.getText().toString().trim());
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private void addOfferSave(String userId, String vid_Selected_items, String order_list,
                              String order_price, String discount, String discount_price,
                              String total, String house_number,
                              String city, String state, String landmark, String booking_slot,
                              String booked_date, String payment) {

        float s = shoppingAdapter.getTotalPrice();

        if (commision.equals("") || commision.equals(null)) {
            commision = "0";
        } else {
            if (commision.length() > 0 && commision.charAt(commision.length() - 1) == '%') {
                commision = commision.substring(0, commision.length() - 1);
            }
            int discount1 = Integer.parseInt(commision);
            double res = ((s / 100.0f) * discount1);
            commision = String.valueOf(res);
        }
        float f1 = Float.parseFloat(discount_price);
        float discounts = Float.parseFloat((String.format("%.01f", f1)));
        float discountsPrice = s - discounts;
        float comm = Float.parseFloat(commision);
        float totalPrice = s - comm;

        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AddFavouriteResponse> call = apiInterface.addOfferSave(userId, vid_Selected_items,
                order_list, order_price, String.valueOf(discounts), String.valueOf(discountsPrice),
                commision, String.valueOf(totalPrice), house_number, city, state, landmark, booking_slot, booked_date, payment);

        call.enqueue(new Callback<AddFavouriteResponse>() {
            @Override
            public void onResponse(Call<AddFavouriteResponse> call, final Response<AddFavouriteResponse> response) {
                pd.dismiss();
                //  Log.d("" + TAG, "" + response);
                try {
                    if (response.isSuccessful()) {
                        AddFavouriteResponse responsee = response.body();
                        if (responsee != null) {
                            String message = responsee.getMessage();
                            int status = responsee.getStatus();
                            if (status == 1) {
                                myCartArrayList.clear();
                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(() -> {
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    startActivity(intent);
                                }, 1000);
                            } else {
                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
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

        //Testing
//        getChecksum("yPwaWl81287534381090", orderId, "cust123", "9799224434", "staddleservices@gmail.com",
//                "WAP", total, "WEBSTAGING", "Retail", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);

//       CallBack Url Staging Environment:
//        "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=<order_id>"
//        CallBack Url Production Environment:
//        "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=<order_id>"

        // Live
        getChecksum("pLxUTB00403908468779", orderId, "cust123", "9799224434", "staddleservices@gmail.com",
                "WAP", total, "WEBSTAGING", "Retail", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
    }

    public String generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder s = new StringBuilder("order");
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
                Log.e("Response:", "Response:" + response.message());
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
                Log.e("Throwable:", "Throwable:" + t.toString());
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

        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) params);
        pgService.initialize(Order, null);
        pgService.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getActivity(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onTransactionResponse(Bundle inResponse) {
                String message = inResponse.getString("RESPMSG");
                addOrder("Payment Transaction response " + inResponse.getString("RESPMSG"));
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
    public void addOrder(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("yes", (arg0, arg1) -> {
            if (message.equalsIgnoreCase("Payment Transaction response Txn Success"))
                addOfferSave(userId, vid_Selected_items, data, totalp, discount, per,
                        total, HouseNo, City, State, LandMark, BookSlot, BookDate, "online");
            else
                arg0.dismiss();
        });
        alertDialogBuilder.setNegativeButton("", (dialog, which) -> {
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }







}
