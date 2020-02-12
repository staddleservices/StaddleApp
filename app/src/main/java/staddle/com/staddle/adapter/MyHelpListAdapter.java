package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.bean.MyHelpList;

public class MyHelpListAdapter extends RecyclerView.Adapter<MyHelpListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MyHelpList> myOrderListModelArrayList;
    private int lastPosition = -1;

    public MyHelpListAdapter(Context mContext, ArrayList<MyHelpList> myOrderListModelArrayList) {
        this.mContext = mContext;
        this.myOrderListModelArrayList = myOrderListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_help, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyHelpList myOrderListModel = myOrderListModelArrayList.get(position);
        setAnimation(holder.itemView, position);

        //holder.txtName.setText(myOrderListModel.getName());
        holder.txtComment.setText(myOrderListModel.getComment());

        if (myOrderListModel.getName().equalsIgnoreCase("admin")) {
            holder.txtTag.setText("Admin");
            holder.txtTag.setGravity(Gravity.END);
            //holder.llMessage.setGravity(Gravity.END);
        } else {
            holder.txtTag.setText("Me");
            holder.txtTag.setGravity(Gravity.START);
           // holder.llMessage.setGravity(Gravity.START);
        }

    }

    @Override
    public int getItemCount() {
        return myOrderListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //TextView txtName;
        TextView txtComment, txtTag;
//        LinearLayout llMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //txtName = itemView.findViewById(R.id.txtName);
            txtComment = itemView.findViewById(R.id.txtComment);
            txtTag = itemView.findViewById(R.id.txtTag);
            //llMessage = itemView.findViewById(R.id.llMessage);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

}
