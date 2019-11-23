package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;

public class MenuOfferListAdapter extends RecyclerView.Adapter<MenuOfferListAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<GetVendorSubCategoryMenuListModule.MenuList> getVendorSubCategoryListModuleList;
    private OnItemClickListener onItemClickListener;
    private String vname;

    MenuOfferListAdapter(Activity context, ArrayList<GetVendorSubCategoryMenuListModule.MenuList> getVendorSubCategoryListModuleList, String vname) {
        this.context = context;
        this.getVendorSubCategoryListModuleList = getVendorSubCategoryListModuleList;
        this.vname = vname;
    }

    @NonNull
    @Override
    public MenuOfferListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_menu_offers, parent, false);
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
        holder.txt_price.setText(menuList.getMenu_price());

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
            GetVendorSubCategoryMenuListModule.MenuList menuList1 = getVendorSubCategoryListModuleList.get(position);
            String vid = menuList1.getVid();
            menuList1.setMenu_name(menuList.getMenu_name());
            menuList1.setMenu_price(menuList.getMenu_price());
            menuList1.setCount(1);
            menuList1.setVid(vid);
            menuList1.setTotalPrice(Integer.parseInt(menuList.getMenu_price()));

            holder.tv_quantity.setText(" " + menuList1.getCount());
            getVendorSubCategoryListModuleList.set(position, menuList1);
            holder.layout_inc.setVisibility(View.VISIBLE);
            holder.ll_add.setVisibility(View.GONE);
            ((VendorDetailsActivityNew) context).updateCart(VendorSubCategoryListAdapter.getCartList());
        });

        holder.img_increase.setOnClickListener(view1 -> {
            GetVendorSubCategoryMenuListModule.MenuList menuList2 = getVendorSubCategoryListModuleList.get(position);

            int count = menuList2.getCount() + 1;

            holder.tv_quantity.setText(" " + count);
            int res = Integer.parseInt(menuList2.getMenu_price());
            int totalPrice = count * res;

            menuList2.setMenu_name(menuList2.getMenu_name());
            menuList2.setMenu_price(menuList2.getMenu_price());
            menuList2.setCount(count);
            menuList2.setVid(menuList2.getVid());
            menuList2.setTotalPrice(totalPrice);
            getVendorSubCategoryListModuleList.set(position, menuList2);
        });

        holder.img_decrease.setOnClickListener(view12 -> {
            GetVendorSubCategoryMenuListModule.MenuList menuList2 = getVendorSubCategoryListModuleList.get(position);
            int count = menuList2.getCount();
            if (count != 0) {
                count = count - 1;
                holder.tv_quantity.setText("" + count);
                int totalPrice = menuList2.getTotalPrice() - Integer.parseInt(menuList2.getMenu_price());

                menuList2.setMenu_name(menuList2.getMenu_name());
                menuList2.setMenu_price(menuList2.getMenu_price());
                menuList2.setCount(count);
                menuList2.setVid(menuList2.getVid());
                menuList2.setTotalPrice(totalPrice);
                getVendorSubCategoryListModuleList.set(position, menuList2);
            }
            if (count == 0) {
                menuList2.setMenu_name(menuList2.getMenu_name());
                menuList2.setMenu_price(menuList2.getMenu_price());
                menuList2.setCount(count);
                menuList2.setVid(menuList2.getVid());
                menuList2.setTotalPrice(Integer.parseInt(menuList2.getMenu_price()));
                getVendorSubCategoryListModuleList.set(position, menuList2);
                notifyDataSetChanged();
                ((VendorDetailsActivityNew) context).updateCart(VendorSubCategoryListAdapter.getCartList());
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
}
