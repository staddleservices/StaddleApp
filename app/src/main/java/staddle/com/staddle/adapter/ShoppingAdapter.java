package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.bean.GetVendorSubCategoryMenuListModule;
import staddle.com.staddle.bean.SubcategoryTreeListModel;
import staddle.com.staddle.fragment.ShoppingFragment;

import static staddle.com.staddle.fragment.ShoppingFragment.AddressLayoutCheckout;
import static staddle.com.staddle.fragment.ShoppingFragment.couponlayout;
import static staddle.com.staddle.fragment.ShoppingFragment.mainContainerLayout;
import static staddle.com.staddle.fragment.ShoppingFragment.paychckoutlayout;
import static staddle.com.staddle.fragment.ShoppingFragment.txtEmptyCart;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.MyViewHolder> {

    private Context mContext;
    private ShoppingAdapter.OnItemClickListener listener;
    private ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myOrderListModelArrayList;
    private int lastPosition = -1;
    private PlusButtonClickListener plusButtonClickListener;

    public ShoppingAdapter(Context mContext, ArrayList<GetVendorSubCategoryMenuListModule.MenuList> myOrderListModelArrayList) {
        this.mContext = mContext;
        this.myOrderListModelArrayList = myOrderListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_shopping, parent, false);
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
            GetVendorSubCategoryMenuListModule.MenuList menuList2 = myOrderListModelArrayList.get(position);
            int count = menuList2.getCount() + 1;
            holder.tv_quantity_old.setText(" " + count);
            int res = Integer.parseInt(menuList2.getMenu_price());
            int totalPrice = count * res;
            menuList2.setMenu_name(menuList2.getMenu_name());
            menuList2.setMenu_price(menuList2.getMenu_price());
            menuList2.setCount(count);
            menuList2.setVid(menuList2.getVid());
            menuList2.setTotalPrice(totalPrice);
            myOrderListModelArrayList.set(position, menuList2);

            notifyDataSetChanged();
            try {
                plusButtonClickListener.onPlusButtonClickListener(position, myOrderListModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        holder.img_decrease.setOnClickListener(view -> {

            int  ci=myOrderListModelArrayList.size();
            if(ci ==0){
                mainContainerLayout.setVisibility(View.GONE);
                txtEmptyCart.setVisibility(View.VISIBLE);
                couponlayout.setVisibility(View.GONE);
                paychckoutlayout.setVisibility(View.GONE);
            }else{
                mainContainerLayout.setVisibility(View.VISIBLE);
                txtEmptyCart.setVisibility(View.GONE);
                couponlayout.setVisibility(View.VISIBLE);
                paychckoutlayout.setVisibility(View.VISIBLE);
                GetVendorSubCategoryMenuListModule.MenuList menuList2 = myOrderListModelArrayList.get(position);
                int count = menuList2.getCount();
                if (count != 0) {
                    count = count - 1;
                    holder.tv_quantity_old.setText("" + count);
                    int totalPrice = menuList2.getTotalPrice() - Integer.parseInt(menuList2.getMenu_price());
                    menuList2.setMenu_name(menuList2.getMenu_name());
                    menuList2.setMenu_price(menuList2.getMenu_price());
                    menuList2.setCount(count);
                    menuList2.setVid(menuList2.getVid());
                    menuList2.setTotalPrice(totalPrice);
                    myOrderListModelArrayList.set(position, menuList2);

                }
                if (count == 0) {
                    listener.onItemClick(myOrderListModel, position);
                    int  i=myOrderListModelArrayList.size();
                    if(i==0){
                        mainContainerLayout.setVisibility(View.GONE);
                        txtEmptyCart.setVisibility(View.VISIBLE);
                        couponlayout.setVisibility(View.GONE);
                        paychckoutlayout.setVisibility(View.GONE);
                    }

                }else {




                    notifyDataSetChanged();
                    try {
                        plusButtonClickListener.onPlusButtonClickListener(position, myOrderListModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        });

//        holder.iv_delete.setOnClickListener(view -> {
//            GetVendorSubCategoryMenuListModule.MenuList menuList2 = myOrderListModelArrayList.get(position);
//            holder.tv_quantity_old.setText("" + 0);
//            int totalPrice = menuList2.getTotalPrice() - Integer.parseInt(menuList2.getMenu_price());
//            menuList2.setMenu_name(menuList2.getMenu_name());
//            menuList2.setMenu_price(menuList2.getMenu_price());
//            menuList2.setCount(0);
//            menuList2.setVid(menuList2.getVid());
//            menuList2.setTotalPrice(totalPrice);
//            myOrderListModelArrayList.set(position, menuList2);
//            listener.onItemClick(myOrderListModel, position);
//            notifyDataSetChanged();
//            try {
//                plusButtonClickListener.onPlusButtonClickListener(position, myOrderListModel);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        });


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

        void bind(final GetVendorSubCategoryMenuListModule.MenuList mInbox, final int position, final ShoppingAdapter.OnItemClickListener listener) {
            try {
                //iv_delete.setOnClickListener(v -> listener.onItemClick(mInbox, position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getTotalPrice() {
        int total = 0;
        for (int i = 0; i < myOrderListModelArrayList.size(); i++) {
            int cprice = myOrderListModelArrayList.get(i).getTotalPrice();
            total += cprice;
        }
        return total;
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    public interface PlusButtonClickListener {
        void onPlusButtonClickListener(int position, GetVendorSubCategoryMenuListModule.MenuList menuList);
    }

    public void setCPlusButtonClickListener(PlusButtonClickListener listener) {
        this.plusButtonClickListener = listener;
    }

    public void setOnItemClickListener(ShoppingAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(GetVendorSubCategoryMenuListModule.MenuList subcategoryTreeListModel, int position);
    }
}