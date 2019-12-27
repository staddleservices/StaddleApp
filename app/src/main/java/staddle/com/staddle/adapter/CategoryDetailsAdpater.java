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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.bean.ProductListCategoryModel;
import staddle.com.staddle.bean.VendorListModel;

import static staddle.com.staddle.retrofitApi.BaseApi.IMAGE_BASE_POST_URL;

public class CategoryDetailsAdpater extends RecyclerView.Adapter<CategoryDetailsAdpater.MyViewHolder> {

    private Context mContext;
    private int lastPosition = -1;
    private ArrayList<ProductListCategoryModel> productListCategoryModelArrayList;
    private ArrayList<ProductListCategoryModel> productListCategoryModelListFilter;
    private OnItemClickListener listener;

    public CategoryDetailsAdpater(Context mContext, ArrayList<ProductListCategoryModel> productListCategoryModelArrayList) {
        this.mContext = mContext;
        this.productListCategoryModelArrayList = productListCategoryModelArrayList;
        productListCategoryModelListFilter = new ArrayList<>(productListCategoryModelArrayList);//new chg
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_recy_swiggy_like_vendorlist, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        try {
            final ProductListCategoryModel productListCategoryModel = productListCategoryModelArrayList.get(position);
            holder.bind(productListCategoryModel, listener);
            setAnimation(holder.itemView, position);
            final String image = IMAGE_BASE_POST_URL + productListCategoryModel.getVimage();

            holder.txtName.setText(productListCategoryModel.getProduct_name());
            holder.txt_location.setText(productListCategoryModel.getVlocation());
            if (!productListCategoryModel.getOffer_price().equals(""))
                holder.txt_discount.setText(productListCategoryModel.getOffer_price() + " Off");
            else
                holder.txt_discount.setText(productListCategoryModel.getOffer_price());

            holder.txtTiming.setText(productListCategoryModel.getOffer_start_date() + " To " + productListCategoryModel.getOffer_end_date());

            holder.tv_item_rating.setText(productListCategoryModel.getRating());



            if (image.isEmpty()) {
                holder.iv_offer_image.setImageResource(R.drawable.makeover);
            } else {
                Picasso.get()
                        .load(image)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.iv_offer_image);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productListCategoryModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txt_location, txt_discount, txtTiming, tv_item_rating;
        ImageView iv_offer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);//
            txt_location = itemView.findViewById(R.id.txt_location);//
            txt_discount = itemView.findViewById(R.id.tv_discount);//
            txtTiming = itemView.findViewById(R.id.txtTiming);//
            tv_item_rating = itemView.findViewById(R.id.tv_item_rating);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
        }

        void bind(final ProductListCategoryModel mInbox, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(mInbox));
        }
    }

    public void setOnItemClickListener(CategoryDetailsAdpater.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductListCategoryModel productListCategoryModel);
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);

        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    public void updateList(ArrayList<ProductListCategoryModel> list) {
        productListCategoryModelArrayList = list;
        notifyDataSetChanged();
    }
}
