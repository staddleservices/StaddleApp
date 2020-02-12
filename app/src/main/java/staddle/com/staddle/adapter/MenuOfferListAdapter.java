package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.activity.PromoCodeActivity;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.bean.CurrentOrderMetaData;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.fcm.DBManager;
import staddle.com.staddle.sheardPref.AppPreferences;

import static staddle.com.staddle.activity.VendorDetailsActivityNew.CR_OR_META_DATA;
import static staddle.com.staddle.activity.VendorDetailsActivityNew.currentOrderMetaData;
import static staddle.com.staddle.fragment.CartFragment.nested_scroll_view_cart;
import static staddle.com.staddle.fragment.CartFragment.paychckoutlayout;
import static staddle.com.staddle.fragment.CartFragment.txtEmptyCart;


public class MenuOfferListAdapter extends RecyclerView.Adapter<MenuOfferListAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<GetVendorSubCategoryMenuListModule.MenuList> getVendorSubCategoryListModuleList;
    private OnItemClickListener onItemClickListener;
    private String vname;
    DBManager dbManager;








    MenuOfferListAdapter(Activity context, ArrayList<GetVendorSubCategoryMenuListModule.MenuList> getVendorSubCategoryListModuleList, String vname) {
        this.context = context;
        this.getVendorSubCategoryListModuleList = getVendorSubCategoryListModuleList;
        this.vname = vname;


    }

    @NonNull
    @Override
    public MenuOfferListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_menu_offers, parent, false);
        dbManager =new DBManager(context);

        return new MenuOfferListAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuOfferListAdapter.MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final GetVendorSubCategoryMenuListModule.MenuList menuList = getVendorSubCategoryListModuleList.get(position);

        if (!vname.equals(HomeActivity.vname)) {
            HomeActivity.vname = vname;
            HomeActivity.myCartArrayList.clear();
        }

        holder.txt_menu_name.setText(menuList.getMenu_name());
        holder.txt_price.setText(menuList.getMenu_price()+"");

        if (menuList.getCount() == 0) {
            holder.layout_inc.setVisibility(View.GONE);
            holder.ll_add.setVisibility(View.VISIBLE);
            //VendorDetailsActivityNew.img_cart.setVisibility(View.GONE);
        } else {
            holder.tv_quantity.setText(" " + menuList.getCount());
            holder.layout_inc.setVisibility(View.VISIBLE);
            holder.ll_add.setVisibility(View.GONE);
            //VendorDetailsActivityNew.img_cart.setVisibility(View.VISIBLE);
        }

        holder.ll_add.setOnClickListener(view -> {

//            if(currentOrderMetaData==null){
//                currentOrderMetaData = new ArrayList<>();
//            }else{
//                currentOrderMetaData = new ArrayList<>();
//            }
            //currentOrderMetaData.clear();
            CurrentOrderMetaData currentOrderMetaDataa = getArrayList(CR_OR_META_DATA);




            if(currentOrderMetaDataa!=null){

                if(currentOrderMetaDataa.getCROR_VID().equals(VendorDetailsActivityNew.vid)){
                    //keep adding
                    //currentOrderMetaData = new ArrayList<>();
                    GetVendorSubCategoryMenuListModule.MenuList menuList1 = getVendorSubCategoryListModuleList.get(position);
                    String vid = menuList1.getVid();
                    menuList1.setMenu_name(menuList.getMenu_name());
                    menuList1.setMenu_price(menuList.getMenu_price());
                    menuList1.setCount(1);
                    menuList1.setVid(vid);

                    menuList1.setTotalPrice(menuList.getMenu_price());
                    dbManager.open();
                    dbManager.insert(String.valueOf(menuList.getId()),vid,String.valueOf(menuList.getMenu_price()),menuList.getMenu_name(),"1",String.valueOf(menuList.getTotalPrice()));
                    dbManager.close();
                    holder.tv_quantity.setText(" " + menuList1.getCount());
                    getVendorSubCategoryListModuleList.set(position, menuList1);
                    holder.layout_inc.setVisibility(View.VISIBLE);
                    holder.ll_add.setVisibility(View.GONE);
                    ((VendorDetailsActivityNew) context).updateCart(VendorSubCategoryListAdapter.getCartList());
                    CurrentOrderMetaData  currentOrderMetaData =new CurrentOrderMetaData(VendorDetailsActivityNew.discount,VendorDetailsActivityNew.tag,VendorDetailsActivityNew.Category,VendorDetailsActivityNew.cid,VendorDetailsActivityNew.vid,VendorDetailsActivityNew.vname,VendorDetailsActivityNew.commision);
                    Gson gson = new Gson();
                    String json = gson.toJson(currentOrderMetaData);
                    AppPreferences.savePreferences(context,"CR_OR_META_DATA",json);
                }else{
                    //show box
                    //already exist

                    new AlertDialog.Builder(context)
                            .setTitle("Clear Cart and Add New?")
                            .setCancelable(false)
                            .setMessage("You have already another providers items in your cart. Do you want to overwrite the cart?")
                            .setPositiveButton("Clear And Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbManager.open();
                                    dbManager.truncate();
                                    dbManager.close();

                                    AppPreferences.deletePref(context,CR_OR_META_DATA);

                                    GetVendorSubCategoryMenuListModule.MenuList menuList1 = getVendorSubCategoryListModuleList.get(position);
                                    String vid = menuList1.getVid();
                                    menuList1.setMenu_name(menuList.getMenu_name());
                                    menuList1.setMenu_price(menuList.getMenu_price());
                                    menuList1.setCount(1);
                                    menuList1.setVid(vid);

                                    menuList1.setTotalPrice(menuList.getMenu_price());
                                    dbManager.open();
                                    dbManager.insert(String.valueOf(menuList.getId()),vid,String.valueOf(menuList.getMenu_price()),menuList.getMenu_name(),"1",String.valueOf(menuList.getTotalPrice()));
                                    dbManager.close();
                                    holder.tv_quantity.setText(" " + menuList1.getCount());
                                    getVendorSubCategoryListModuleList.set(position, menuList1);
                                    holder.layout_inc.setVisibility(View.VISIBLE);
                                    holder.ll_add.setVisibility(View.GONE);
                                    ((VendorDetailsActivityNew) context).updateCart(VendorSubCategoryListAdapter.getCartList());
                                    CurrentOrderMetaData  currentOrderMetaData =new CurrentOrderMetaData(VendorDetailsActivityNew.discount,VendorDetailsActivityNew.tag,VendorDetailsActivityNew.Category,VendorDetailsActivityNew.cid,VendorDetailsActivityNew.vid,VendorDetailsActivityNew.vname,VendorDetailsActivityNew.commision);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(currentOrderMetaData);
                                    AppPreferences.savePreferences(context,"CR_OR_META_DATA",json);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }


            }else{
                //not exist;

                //currentOrderMetaData = new ArrayList<>();
                GetVendorSubCategoryMenuListModule.MenuList menuList1 = getVendorSubCategoryListModuleList.get(position);
                String vid = menuList1.getVid();
                menuList1.setMenu_name(menuList.getMenu_name());
                menuList1.setMenu_price(menuList.getMenu_price());
                menuList1.setCount(1);
                menuList1.setVid(vid);
                menuList1.setTotalPrice(menuList.getMenu_price());
                dbManager.open();
                dbManager.insert(String.valueOf(menuList.getId()),vid,String.valueOf(menuList.getMenu_price()),menuList.getMenu_name(),"1",String.valueOf(menuList.getTotalPrice()));
                dbManager.close();
                holder.tv_quantity.setText(" " + menuList1.getCount());
                getVendorSubCategoryListModuleList.set(position, menuList1);
                holder.layout_inc.setVisibility(View.VISIBLE);
                holder.ll_add.setVisibility(View.GONE);
                ((VendorDetailsActivityNew) context).updateCart(VendorSubCategoryListAdapter.getCartList());
                CurrentOrderMetaData  currentOrderMetaData =new CurrentOrderMetaData(VendorDetailsActivityNew.discount,VendorDetailsActivityNew.tag,VendorDetailsActivityNew.Category,VendorDetailsActivityNew.cid,VendorDetailsActivityNew.vid,VendorDetailsActivityNew.vname,VendorDetailsActivityNew.commision);
                Gson gson = new Gson();
                String json = gson.toJson(currentOrderMetaData);
                AppPreferences.savePreferences(context,"CR_OR_META_DATA",json);
            }


        });

        holder.img_increase.setOnClickListener(view1 -> {
            GetVendorSubCategoryMenuListModule.MenuList menuList2 = getVendorSubCategoryListModuleList.get(position);
            String vid = menuList2.getVid();
            int count = menuList2.getCount() + 1;

            holder.tv_quantity.setText(" " + count);
            double res = menuList2.getMenu_price();
            double totalPrice = count * res;

            menuList2.setMenu_name(menuList2.getMenu_name());
            menuList2.setMenu_price(menuList2.getMenu_price());
            menuList2.setCount(count);

            menuList2.setVid(menuList2.getVid());
            menuList2.setTotalPrice(totalPrice);
            dbManager.open();
            dbManager.update(String.valueOf(menuList.getId()),vid,String.valueOf(menuList.getMenu_price()),menuList.getMenu_name(),String.valueOf(count),String.valueOf(totalPrice));
            dbManager.close();
            getVendorSubCategoryListModuleList.set(position, menuList2);
        });

        holder.img_decrease.setOnClickListener(view12 -> {


            GetVendorSubCategoryMenuListModule.MenuList menuList2 = getVendorSubCategoryListModuleList.get(position);
            int count = menuList2.getCount();
            String vid = menuList2.getVid();

            Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();

            if (count != 0) {
                count = count - 1;
                holder.tv_quantity.setText("" + count);
                double totalPrice = menuList2.getTotalPrice() - menuList2.getMenu_price();

                menuList2.setMenu_name(menuList2.getMenu_name());
                menuList2.setMenu_price(menuList2.getMenu_price());
                menuList2.setCount(count);
                Log.d("CARTSIZE","cash"+getVendorSubCategoryListModuleList.size());
                menuList2.setVid(menuList2.getVid());
                menuList2.setTotalPrice(totalPrice);
                dbManager.open();
                dbManager.update(String.valueOf(menuList.getId()),vid,String.valueOf(menuList.getMenu_price()),menuList.getMenu_name(),String.valueOf(count),String.valueOf(totalPrice));
                dbManager.close();
                getVendorSubCategoryListModuleList.set(position, menuList2);
            }
            if (count == 0) {





                menuList2.setMenu_name(menuList2.getMenu_name());
                menuList2.setMenu_price(menuList2.getMenu_price());
                menuList2.setCount(count);
                menuList2.setVid(menuList2.getVid());
                menuList2.setTotalPrice(menuList2.getMenu_price());
                dbManager.open();

                dbManager.delete(String.valueOf(menuList2.getId()));

                getVendorSubCategoryListModuleList.set(position, menuList2);
                notifyDataSetChanged();
                ((VendorDetailsActivityNew) context).updateCart(VendorSubCategoryListAdapter.getCartList());

                dbManager.close();

            }
        });

    }

    @Override
    public int getItemCount() {
        return getVendorSubCategoryListModuleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_price, txt_menu_name, tv_quantity;
        RelativeLayout ll_add, layout_inc;
        ImageView img_decrease, img_increase;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_menu_name = itemView.findViewById(R.id.txt_menu_name);
            ll_add = itemView.findViewById(R.id.ll_add);
            layout_inc = itemView.findViewById(R.id.layout_inc);
            img_decrease = itemView.findViewById(R.id.img_decrease);
            img_increase = itemView.findViewById(R.id.img_increase);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);

        }

        void bind(final GetVendorSubCategoryMenuListModule.MenuList mInbox, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), mInbox));
        }
    }

    public void setOnItemClickListener(MenuOfferListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, GetVendorSubCategoryMenuListModule.MenuList menuListt);
    }

    private void show_warning_box(){


    }

    public CurrentOrderMetaData getArrayList(String key){

        Gson gson = new Gson();
        String json = AppPreferences.loadPreferences(context,key);
        return  gson.fromJson(json, CurrentOrderMetaData.class);
    }


}
