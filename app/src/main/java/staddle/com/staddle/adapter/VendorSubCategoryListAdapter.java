package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import staddle.com.staddle.HomeActivity;
import staddle.com.staddle.R;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;

public class VendorSubCategoryListAdapter extends RecyclerView.Adapter<VendorSubCategoryListAdapter.MyViewHolder> {

    private Activity context;
    private static List<GetVendorSubCategoryMenuListModule> getVendorSubCategoryListModuleList;
    String id = "";
    private RecyclerView rvSubCatMenuOffers;
    private String isServiceType;
    private OnItemClickListener onItemClickListener;
    private int lastPosition = -1;
    private String venderName;

    public VendorSubCategoryListAdapter(Activity context, List<GetVendorSubCategoryMenuListModule> getVendorSubCategoryListModuleList, String isServiceType, String venderName) {
        this.context = context;
        VendorSubCategoryListAdapter.getVendorSubCategoryListModuleList = getVendorSubCategoryListModuleList;
        this.isServiceType = isServiceType;
        this.venderName = venderName;
    }

    @NonNull
    @Override
    public VendorSubCategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_menu_adapter, parent, false);
        return new VendorSubCategoryListAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VendorSubCategoryListAdapter.MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        GetVendorSubCategoryMenuListModule vendorSubCategoryListModule = getVendorSubCategoryListModuleList.get(position);
        holder.bind(vendorSubCategoryListModule, onItemClickListener, position);
        if (isServiceType.equalsIgnoreCase("isServiceType")) {
            int MenuOffersize = vendorSubCategoryListModule.getMenu().size();
            holder.tvCategory_name.setText(vendorSubCategoryListModule.getVender_sub_catgoey());
            holder.txt_menu_count.setVisibility(View.VISIBLE);
            holder.txt_menu_count.setText("   " + MenuOffersize);
            rvSubCatMenuOffers.setVisibility(View.GONE);
        } else {
            rvSubCatMenuOffers.setVisibility(View.VISIBLE);
            holder.txt_menu_count.setVisibility(View.GONE);
            holder.tvCategory_name.setText(vendorSubCategoryListModule.getVender_sub_catgoey());
            MenuOfferListAdapter menuListAdapter = new MenuOfferListAdapter(context, vendorSubCategoryListModule.getMenu(), venderName);
            rvSubCatMenuOffers.setLayoutManager(new LinearLayoutManager(context));
            rvSubCatMenuOffers.setHasFixedSize(true);
            rvSubCatMenuOffers.setAdapter(menuListAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return getVendorSubCategoryListModuleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory_name, txt_menu_count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory_name = itemView.findViewById(R.id.txt_menu_name);
            txt_menu_count = itemView.findViewById(R.id.txt_menu_count);
            rvSubCatMenuOffers = itemView.findViewById(R.id.rvSubCatMenuOffers);
        }

        void bind(final GetVendorSubCategoryMenuListModule mInbox, final OnItemClickListener listener, int position) {
            itemView.setOnClickListener(v -> listener.onItemClick(position, mInbox));
        }
    }

    public void setOnItemClickListener(VendorSubCategoryListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, GetVendorSubCategoryMenuListModule menuListt);
    }

    public static int getCartList() {
        HomeActivity.myCartArrayList.clear();
        for (int i = 0; i < getVendorSubCategoryListModuleList.size(); i++) {
            for (GetVendorSubCategoryMenuListModule.MenuList menuList : getVendorSubCategoryListModuleList.get(i).getMenu()) {
                if (menuList.getCount() != 0) {
                    HomeActivity.myCartArrayList.add(menuList);
                }
            }
        }
        return HomeActivity.myCartArrayList.size();
    }

}


