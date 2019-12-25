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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.bean.FavouriteListModel;
import staddle.com.staddle.retrofitApi.BaseApi;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<FavouriteListModel> favouriteListModelArrayList;
    private int lastPosition = -1;


    public FavouriteListAdapter(Context mContext, ArrayList<FavouriteListModel> favouriteListModelArrayList) {
        this.mContext = mContext;
        this.favouriteListModelArrayList = favouriteListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_recy_swiggy_like_vendorlist, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FavouriteListModel favouriteListModel = favouriteListModelArrayList.get(position);
        setAnimation(holder.itemView, position);

        holder.txt_name.setText(favouriteListModel.getBusiness_name());
        holder.txt_location.setText(favouriteListModel.getLocation());
        if (!favouriteListModel.getUser_discount().equals(""))
            holder.txt_discount.setText(favouriteListModel.getUser_discount() + " Off");
        else
            holder.txt_discount.setText(favouriteListModel.getUser_discount());

        holder.txtTiming.setText(favouriteListModel.getOpening_time() + " To " + favouriteListModel.getClosing_time());
        holder.tv_item_rating.setText(favouriteListModel.getRating());

        String image_url = BaseApi.IMAGE_BASE_POST_URL + favouriteListModel.getImage();
        Picasso.get()
                .load(image_url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.iv_offer_image);


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, VendorDetailsActivityNew.class);
            if (favouriteListModelArrayList.get(position).getCid().equals("1")) {
                intent.putExtra("Tag", "HomeVender");
                intent.putExtra("Category", favouriteListModelArrayList.get(position).getSub_category());
            } else if (!favouriteListModelArrayList.get(position).getCid().equals("2")) {
                intent.putExtra("Tag", "HOUSE");
            } else if (!favouriteListModelArrayList.get(position).getCid().equals("3")) {
                intent.putExtra("Tag", "Security");
            }
            intent.putExtra("vname", favouriteListModelArrayList.get(position).getBusiness_name());
            intent.putExtra("location", favouriteListModelArrayList.get(position).getLocation());
            intent.putExtra("rating", favouriteListModelArrayList.get(position).getRating());
            intent.putExtra("image", favouriteListModelArrayList.get(position).getImage());
            intent.putExtra("vid", favouriteListModelArrayList.get(position).getVid());
            intent.putExtra("cid", favouriteListModelArrayList.get(position).getCid());
            intent.putExtra("closingTime", favouriteListModelArrayList.get(position).getClosing_time());
            intent.putExtra("openingTime", favouriteListModelArrayList.get(position).getOpening_time());
            intent.putExtra("image1", favouriteListModelArrayList.get(position).getImage1());
            intent.putExtra("image2", favouriteListModelArrayList.get(position).getImage2());
            intent.putExtra("image3", favouriteListModelArrayList.get(position).getImage3());
            intent.putExtra("image4", favouriteListModelArrayList.get(position).getImage4());
            intent.putExtra("image5", favouriteListModelArrayList.get(position).getImage5());
            intent.putExtra("subcat", favouriteListModelArrayList.get(position).getSub_category());
            intent.putExtra("discount", favouriteListModelArrayList.get(position).getUser_discount());
            intent.putExtra("commision", favouriteListModelArrayList.get(position).getCommission());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favouriteListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_location, txt_discount, txtTiming, tv_item_rating;
        ImageView iv_offer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_location = itemView.findViewById(R.id.txt_location);
            txt_discount = itemView.findViewById(R.id.tv_discount);
            tv_item_rating = itemView.findViewById(R.id.tv_item_rating);
            txtTiming = itemView.findViewById(R.id.txtTiming);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }
}
