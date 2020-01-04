package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import staddle.com.staddle.bean.FiltersDataModel;
import staddle.com.staddle.bean.MyOrderListModel;
import staddle.com.staddle.bean.NotificationsDataModels;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<NotificationsDataModels> notificationsDataModels;
    private int lastPosition = -1;

    public NotificationsAdapter(Context mContext, ArrayList<NotificationsDataModels> dataModels) {
        this.mContext = mContext;
        this.notificationsDataModels = dataModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notificationsitem, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(notificationsDataModels.get(position).getStatus().equals("A")){
            holder.title.setText("Order Confirmed");
        }else if(notificationsDataModels.get(position).getStatus().equals("C")){
            holder.title.setText("Order Completed");
        }

        holder.content.setText(notificationsDataModels.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notificationsDataModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView itemholder;
        TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationtitle);
            content = itemView.findViewById(R.id.notificationcontent);
            itemholder= itemView.findViewById(R.id.vendorfiltercardview);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

}

