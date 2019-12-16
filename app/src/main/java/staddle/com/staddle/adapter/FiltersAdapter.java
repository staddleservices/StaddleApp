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

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<FiltersDataModel> filtersDataModels;
    private int lastPosition = -1;

    public FiltersAdapter(Context mContext, ArrayList<FiltersDataModel> dataModels) {
        this.mContext = mContext;
        this.filtersDataModels = dataModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.applyfilteritem, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.filterName.setText(filtersDataModels.get(position).getFiltername());
    }

    @Override
    public int getItemCount() {
        return filtersDataModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView filterName;
        CardView itemholder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filterName = itemView.findViewById(R.id.filtertext);
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
