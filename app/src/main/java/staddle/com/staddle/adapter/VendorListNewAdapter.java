package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.BaseApi;

public class VendorListNewAdapter extends RecyclerView.Adapter<VendorListNewAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<VendorListModel> vendorListModelArrayList;
    private ArrayList<VendorListModel> vendorListModelArrayListFilter;

    public VendorListNewAdapter(Context context, ArrayList<VendorListModel> vendorListModelArrayList) {
        this.context = context;
        this.vendorListModelArrayList = vendorListModelArrayList;
        vendorListModelArrayListFilter = new ArrayList<>(vendorListModelArrayList);//new chg
    }

    public void setData(ArrayList<VendorListModel> vendorListModelArrayList) {
        this.vendorListModelArrayList = vendorListModelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recy_swiggy_like_vendorlist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return vendorListModelArrayList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final VendorListModel vendorListModel = vendorListModelArrayList.get(position);

        holder.txt_name.setText(vendorListModel.getBusiness_name());
        holder.txt_location.setText(vendorListModel.getLocation());
        if (!vendorListModel.getUser_discount().equals(""))
            holder.txt_discount.setText(vendorListModel.getUser_discount() + " Off");
        else
            holder.txt_discount.setText(vendorListModel.getUser_discount());

        holder.txtTiming.setText(vendorListModel.getOpening_time() + " To " + vendorListModel.getClosing_time());
        //Toast.makeText(context, vendorListModel.getRating(), Toast.LENGTH_SHORT).show();
        if(vendorListModel.getRating().equals("0.0")){
            Log.d("RATING_RUN","if");
            holder.tv_item_rating.setText("New");
        }else  {
            Log.d("RATING_RUN","else");
            holder.tv_item_rating.setText(vendorListModel.getRating());
        }
        //holder.tv_item_rating.setText(vendorListModel.getRating());

        String image_url = BaseApi.IMAGE_BASE_POST_URL + vendorListModel.getImage();
        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_app_logo)
                .error(R.mipmap.ic_launcher)
                .into(holder.iv_offer_image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, VendorDetailsActivityNew.class);
            if (vendorListModelArrayList.get(position).getCid().equals("1")) {
                intent.putExtra("Tag", "HomeVender");
                intent.putExtra("Category", vendorListModelArrayList.get(position).getSub_category());
            } else if (!vendorListModelArrayList.get(position).getCid().equals("2")) {
                intent.putExtra("Tag", "HOUSE");
            } else if (!vendorListModelArrayList.get(position).getCid().equals("3")) {
                intent.putExtra("Tag", "Security");
            }
            intent.putExtra("vname", vendorListModelArrayList.get(position).getBusiness_name());
            intent.putExtra("location", vendorListModelArrayList.get(position).getLocation());
            intent.putExtra("rating", vendorListModelArrayList.get(position).getRating());
            intent.putExtra("image", vendorListModelArrayList.get(position).getImage());
            intent.putExtra("vid", vendorListModelArrayList.get(position).getId());
            intent.putExtra("cid", vendorListModelArrayList.get(position).getCid());
            intent.putExtra("closingTime", vendorListModelArrayList.get(position).getClosing_time());
            intent.putExtra("openingTime", vendorListModelArrayList.get(position).getOpening_time());
            intent.putExtra("image1", vendorListModelArrayList.get(position).getImage1());
            intent.putExtra("image2", vendorListModelArrayList.get(position).getImage2());
            intent.putExtra("image3", vendorListModelArrayList.get(position).getImage3());
            intent.putExtra("image4", vendorListModelArrayList.get(position).getImage4());
            intent.putExtra("image5", vendorListModelArrayList.get(position).getImage5());
            intent.putExtra("subcat", vendorListModelArrayList.get(position).getSub_category());
            intent.putExtra("discount", vendorListModelArrayList.get(position).getUser_discount());
            intent.putExtra("commision", vendorListModelArrayList.get(position).getCommision());
            context.startActivity(intent);
        });
    }



    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraints) { //this method exe in bg thread
            ArrayList<VendorListModel> filteredList = new ArrayList<>();
            if (constraints == null || constraints.length() == 0) {
                filteredList.addAll(vendorListModelArrayListFilter);
            } else {
                String filterpatern = constraints.toString().toLowerCase().trim();

                for (VendorListModel productListCategoryModel : vendorListModelArrayListFilter) {
                    if (productListCategoryModel.getBusiness_name().toLowerCase().contains(filterpatern)) {
                        filteredList.add(productListCategoryModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            vendorListModelArrayList.clear();
            vendorListModelArrayList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_location, txt_discount, txtTiming, tv_item_rating;
        ImageView iv_offer_image;

        MyViewHolder(View view) {
            super(view);
            iv_offer_image = view.findViewById(R.id.iv_offer_image);
            txt_name = view.findViewById(R.id.txt_name);
            txt_location = view.findViewById(R.id.txt_location);
            txt_discount = view.findViewById(R.id.tv_discount);
            tv_item_rating = view.findViewById(R.id.tv_item_rating);
            txtTiming = view.findViewById(R.id.txtTiming);
        }
    }
}
