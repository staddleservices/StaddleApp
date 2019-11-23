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

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MyOrderListModel> myOrderListModelArrayList;
    private int lastPosition = -1;

    public MyOrderListAdapter(Context mContext, ArrayList<MyOrderListModel> myOrderListModelArrayList) {
        this.mContext = mContext;
        this.myOrderListModelArrayList = myOrderListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyOrderListModel myOrderListModel = myOrderListModelArrayList.get(position);
        setAnimation(holder.itemView, position);

        if (myOrderListModel.getStatus().equalsIgnoreCase("C") && myOrderListModel.getRating().equalsIgnoreCase("0"))
            holder.btnRate.setVisibility(View.VISIBLE);
        else
            holder.btnRate.setVisibility(View.GONE);

        holder.txtName.setText(myOrderListModel.getvName());
        float tot = Float.parseFloat(myOrderListModel.getOrder_price());
        float comm = Float.parseFloat(myOrderListModel.getDiscount());
        float totalPrice = tot - comm;

        holder.tv_total_amount.setText("₹  " + totalPrice);
        holder.tv_order_date.setText(myOrderListModel.getCreate_date());
        holder.txtBookBook.setText(myOrderListModel.getBooked_date() + " " + myOrderListModel.getBooking_slot());

        if (myOrderListModel.getStatus().equalsIgnoreCase("A"))
            holder.txtStatus.setText("Accepted");
        else if (myOrderListModel.getStatus().equalsIgnoreCase("C"))
            holder.txtStatus.setText("Completed");
        else if (myOrderListModel.getStatus().equalsIgnoreCase("P"))
            holder.txtStatus.setText("Pending");
        else if (myOrderListModel.getStatus().equalsIgnoreCase("R"))
            holder.txtStatus.setText("Rejected");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MyOrderDetailsActivity.class);
            ArrayList<MyOrderListModel.Data> dataArrayList = myOrderListModelArrayList.get(position).getData();

            intent.putParcelableArrayListExtra("DATA_LIST", dataArrayList);
            intent.putExtra("NAME", myOrderListModelArrayList.get(position).getvName());
            intent.putExtra("DATE", myOrderListModelArrayList.get(position).getCreate_date());
            intent.putExtra("ORDER_PRICE", myOrderListModelArrayList.get(position).getOrder_price());
            intent.putExtra("ORDER_STATUS", myOrderListModelArrayList.get(position).getStatus());
            intent.putExtra("DICS_PRICE", myOrderListModelArrayList.get(position).getDiscount_price());
            intent.putExtra("DICSCOUNT", myOrderListModelArrayList.get(position).getDiscount());
            mContext.startActivity(intent);

        });

        holder.btnRate.setOnClickListener(v -> {
            ((MyOrderActivity) mContext).ratingDialog(myOrderListModelArrayList.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return myOrderListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, tv_total_amount, tv_order_date, txtBookBook, txtStatus;
        Button btnRate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            txtBookBook = itemView.findViewById(R.id.txtBookBook);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnRate = itemView.findViewById(R.id.btnRate);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

}
