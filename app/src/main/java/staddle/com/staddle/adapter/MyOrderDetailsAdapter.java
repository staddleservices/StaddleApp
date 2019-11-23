package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.MyOrderActivity;
import staddle.com.staddle.activity.MyOrderDetailsActivity;
import staddle.com.staddle.bean.MyOrderListModel;

public class MyOrderDetailsAdapter extends RecyclerView.Adapter<MyOrderDetailsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MyOrderListModel.Data> dataArrayList;
    private int lastPosition = -1;

    public MyOrderDetailsAdapter(Context mContext, ArrayList<MyOrderListModel.Data> dataArrayList) {
        this.mContext = mContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_order_details, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyOrderListModel.Data myOrderListModel = dataArrayList.get(position);
        setAnimation(holder.itemView, position);

        holder.txtName.setText(myOrderListModel.getMenu_name());
      //  holder.txtOfferPrice.setText("₹ " + myOrderListModel.getMenu_Oprice());
        holder.tv_total_amount.setText("₹ " + myOrderListModel.getMenu_price());
        holder.txtQuantity.setText(myOrderListModel.getCount());


    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtOfferPrice, tv_total_amount, txtQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
           // txtOfferPrice = itemView.findViewById(R.id.txtOfferPrice);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

}
