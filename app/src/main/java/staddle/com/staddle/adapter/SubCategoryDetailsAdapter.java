package staddle.com.staddle.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.bean.SubcategoryTreeListModel;

public class SubCategoryDetailsAdapter extends RecyclerView.Adapter<SubCategoryDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private OnItemClickListener listener;

    private ArrayList<SubcategoryTreeListModel> subCategoryTreeTreeListModelArrayList;

    public SubCategoryDetailsAdapter(Context mContext, ArrayList<SubcategoryTreeListModel> subCategoryModelArrayList) {
        this.mContext = mContext;
        this.subCategoryTreeTreeListModelArrayList = subCategoryModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.applyfilteritem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SubcategoryTreeListModel subCategoryModel = subCategoryTreeTreeListModelArrayList.get(position);
        holder.bind(subCategoryModel, listener);

        String sub_name = subCategoryModel.getSub_name();

        holder.txtName.setText(sub_name);
    }

    @Override
    public int getItemCount() {
        return subCategoryTreeTreeListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.filtertext);
        }

        void bind(final SubcategoryTreeListModel mInbox, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mInbox);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SubcategoryTreeListModel subcategoryTreeListModel);
    }

}
