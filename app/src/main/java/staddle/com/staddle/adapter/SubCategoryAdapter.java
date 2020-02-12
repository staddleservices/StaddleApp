package staddle.com.staddle.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.SubCategoryDetailsActivity;
import staddle.com.staddle.bean.SubCategoryModel;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SubCategoryModel> subCategoryModelArrayList = new ArrayList<>();
    private String beautySaolon;
    private String house;
    private String security;

    public SubCategoryAdapter(Context mContext, ArrayList<SubCategoryModel> subCategoryModelArrayList, String beautySaolon, String house, String security) {
        this.mContext = mContext;
        this.subCategoryModelArrayList = subCategoryModelArrayList;
        this.beautySaolon = beautySaolon;
        this.house = house;
        this.security = security;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_beauty_salon, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SubCategoryModel subCategoryModel = subCategoryModelArrayList.get(position);
        String sub_name = subCategoryModel.getSub_name();
        final String sid = subCategoryModel.getSid();
        final String cid = subCategoryModel.getCid();

        holder.txtSubCategory.setText(sub_name);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SubCategoryDetailsActivity.class);
            intent.putExtra("cid", cid);
            mContext.startActivity(intent);
        });

        if (beautySaolon.equalsIgnoreCase("beauty salon")) {
            holder.iv_beauty.setImageResource(R.drawable.makeover);
        } else if (security.equalsIgnoreCase("security")) {
            holder.iv_beauty.setImageResource(R.drawable.ic_policeman);
        } else {
            holder.iv_beauty.setImageResource(R.drawable.ic_mansion);
        }

    }

    @Override
    public int getItemCount() {
        return subCategoryModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubCategory;
        ImageView iv_beauty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubCategory = itemView.findViewById(R.id.txtSubCategory);
            iv_beauty = itemView.findViewById(R.id.iv_beauty);
        }


    }
}
