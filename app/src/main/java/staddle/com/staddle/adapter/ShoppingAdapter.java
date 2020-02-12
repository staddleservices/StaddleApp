package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.fcm.DBManager;
import staddle.com.staddle.sheardPref.AppPreferences;

import static staddle.com.staddle.activity.VendorDetailsActivityNew.CR_OR_META_DATA;
import static staddle.com.staddle.fragment.CartFragment.discount_value_show;
import static staddle.com.staddle.fragment.CartFragment.item_total_fare;
import static staddle.com.staddle.fragment.CartFragment.nested_scroll_view_cart;
import static staddle.com.staddle.fragment.CartFragment.paychckoutlayout;
import static staddle.com.staddle.fragment.CartFragment.price_saved_text;
import static staddle.com.staddle.fragment.CartFragment.topay_cart;
import static staddle.com.staddle.fragment.CartFragment.txtEmptyCart;


public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.MyViewHolder> {

    private Context mContext;
    private ShoppingAdapter.OnItemClickListener listener;
    private ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myOrderListModelArrayList;
    private int lastPosition = -1;

    float total;
    DBManager dbManager;
    public ShoppingAdapter(Context mContext, ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myOrderListModelArrayList) {
        this.mContext = mContext;
        this.myOrderListModelArrayList = myOrderListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_shopping, parent, false);
        dbManager = new DBManager(mContext);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GetVendorSubCategoryMenuListModule.MenuList myOrderListModel = myOrderListModelArrayList.get(position);
//        holder.bind(myOrderListModel, position, listener);

        holder.tv_name.setText(myOrderListModel.getMenu_name());
        holder.tv_price.setText("" + myOrderListModel.getTotalPrice() + " ");
        holder.tv_quantity_old.setText("" + myOrderListModel.getCount());

        holder.img_increase.setOnClickListener(view -> {
            dbManager.open();
            double newtotal = Double.valueOf(myOrderListModel.getTotalPrice())+Double.valueOf(myOrderListModel.getMenu_price());
            int newQuantity = myOrderListModel.getCount()+1;
            Log.d("SHOPPING ADAPTOR", newtotal+":"+newQuantity);
            dbManager.updateQuantity(myOrderListModel.getId(),String.valueOf(newQuantity),String.valueOf(newtotal));
            myOrderListModel.setCount(newQuantity);
            myOrderListModel.setTotalPrice(newtotal);
            notifyDataSetChanged();

            String discount = AppPreferences.loadPreferences(mContext,"CRORDISCOUNT");
            float total = dbManager.getTotal();




                float topay = total- ((Float.valueOf(discount)/100)*total);
                item_total_fare.setText("₹ "+String.valueOf(total));
                discount_value_show.setText(" - ₹ "+(Float.valueOf(discount)/100)*total);

                topay_cart.setText("₹ "+topay);

                price_saved_text.setText("You have saved ₹ "+(Float.valueOf(discount)/100)*total +" on the bill.");





            dbManager.close();


        });

        holder.img_decrease.setOnClickListener(view -> {
            if(myOrderListModel.getCount()==1){

                dbManager.open();

                dbManager.delete(myOrderListModel.getId());

                float total = dbManager.getTotal();

                String discount = AppPreferences.loadPreferences(mContext,"CRORDISCOUNT");




                float topay = total- ((Float.valueOf(discount)/100)*total);
                item_total_fare.setText("₹ "+String.valueOf(total));
                discount_value_show.setText(" - ₹ "+(Float.valueOf(discount)/100)*total);

                topay_cart.setText("₹ "+topay);

                price_saved_text.setText("You have saved ₹ "+(Float.valueOf(discount)/100)*total +" on the bill.");
                myOrderListModelArrayList.remove(position);
                notifyDataSetChanged();
                dbManager.close();
                Log.d("CARTSIZE","cash"+myOrderListModelArrayList.size());
                dbManager.open();
                if(myOrderListModelArrayList.size()==0){
                    AppPreferences.deletePref(mContext,CR_OR_META_DATA);
                    dbManager.truncate();
                    txtEmptyCart.setVisibility(View.VISIBLE);
                    nested_scroll_view_cart.setVisibility(View.GONE);
                    paychckoutlayout.setVisibility(View.GONE);
                }
                dbManager.close();

            }else{
                dbManager.open();
                double newtotal = Double.valueOf(myOrderListModel.getTotalPrice())-Double.valueOf(myOrderListModel.getMenu_price());
                int newQuantity = myOrderListModel.getCount()-1;
                Log.d("SHOPPING ADAPTOR", newtotal+":"+newQuantity);
                dbManager.updateQuantity(myOrderListModel.getId(),String.valueOf(newQuantity),String.valueOf(newtotal));
                myOrderListModel.setCount(newQuantity);
                myOrderListModel.setTotalPrice(newtotal);
                notifyDataSetChanged();

                float total = dbManager.getTotal();

                String discount = AppPreferences.loadPreferences(mContext,"CRORDISCOUNT");




                float topay = total- ((Float.valueOf(discount)/100)*total);
                item_total_fare.setText("₹ "+String.valueOf(total));
                discount_value_show.setText(" - ₹ "+(Float.valueOf(discount)/100)*total);

                topay_cart.setText("₹ "+topay);

                price_saved_text.setText("You have saved ₹ "+(Float.valueOf(discount)/100)*total +" on the bill.");

                dbManager.close();
            }




        });



    }

    @Override
    public int getItemCount() {
        return myOrderListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_price, tv_quantity_old;
        ImageView img_decrease, img_increase;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity_old = itemView.findViewById(R.id.tv_quantity_old);
            img_decrease = itemView.findViewById(R.id.img_decrease);
            img_increase = itemView.findViewById(R.id.img_increase);


        }


    }

    public int getTotalPrice() {
       return 0;
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }



    public void setOnItemClickListener(ShoppingAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(GetVendorSubCategoryMenuListModule.MenuList subcategoryTreeListModel, int position);
    }
}