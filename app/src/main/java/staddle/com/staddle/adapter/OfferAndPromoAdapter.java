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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.bean.OfferAndPromoModel;

public class OfferAndPromoAdapter extends RecyclerView.Adapter<OfferAndPromoAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<OfferAndPromoModel> offerAndPromoArrayList;

    public OfferAndPromoAdapter(Context context, ArrayList<OfferAndPromoModel> offerAndPromoArrayList) {
        this.context = context;
        this.offerAndPromoArrayList = offerAndPromoArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_favourite, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view); // pass the view to View Holder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        OfferAndPromoModel offerAndPromoModel = offerAndPromoArrayList.get(position);
        final String product_details = offerAndPromoModel.getProduct_details();
        final String current_price = offerAndPromoModel.getCurrent_price();
        final String offer_price = offerAndPromoModel.getOffer_price();
        final String product_name = offerAndPromoModel.getProduct_name();

        myViewHolder.txtDetails.setText(product_details);
        myViewHolder.tv_current_price.setText(current_price);
        myViewHolder.tv_offerprice.setText(offer_price);
        myViewHolder.txtName.setText(product_name);

        String image = offerAndPromoModel.getImage();
        if (image.isEmpty()) {
            myViewHolder.iv_offer_image.setImageResource(R.drawable.makeover);
        } else {
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_hurt)// Place holder image from drawable folder
                    .error(R.drawable.ic_launcher_background)
                    .into(myViewHolder.iv_offer_image);
        }

    }

    @Override
    public int getItemCount() {
        return offerAndPromoArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtDetails, tv_current_price, tv_offerprice;
        ImageView iv_offer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetails = itemView.findViewById(R.id.txt_details);
            tv_current_price = itemView.findViewById(R.id.tv_current_price);
            tv_offerprice = itemView.findViewById(R.id.tv_offerprice);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
        }
    }
}
