package staddle.com.staddle.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import staddle.com.staddle.R;

import staddle.com.staddle.bean.SliderImagesModule;

public class SliderImageAdapter extends RecyclerView.Adapter<SliderImageAdapter.MyViewHolder>  {
    Context context;
    private List<SliderImagesModule> image_arraylist;
    private String cid = "";

    public SliderImageAdapter(Context context, List<SliderImagesModule> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.slidingimages_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            Picasso.get()
                    .load((image_arraylist.get(i).getImage()))
                    .placeholder(R.drawable.saloon1) // optional
                    .error(R.drawable.saloon1)         // optional
                    .into(myViewHolder.iv_offer_image);
            //container.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return image_arraylist.size();
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
//        ImageView image_banner = view.findViewById(R.id.image_banner);
//        SliderImagesModule sliderImagesModule = image_arraylist.get(position);
//        String image = sliderImagesModule.getImage();
//
//        image_banner.setOnClickListener(view1 -> {
//            cid = sliderImagesModule.getCid();
//            if (cid.equalsIgnoreCase("1")) {
//                Intent intent = new Intent(context, AtHomeActivity.class);
//                context.startActivity(intent);
//            } else if (cid.equalsIgnoreCase("2")) {
//                Intent intent = new Intent(context, HouseKeepingActivity.class);
//                context.startActivity(intent);
//            } else if (cid.equalsIgnoreCase("3")) {
//                Intent intent = new Intent(context, SecurityGuardActivity.class);
//                context.startActivity(intent);
//            } else if (cid.equalsIgnoreCase("4")) {
//                Intent intent = new Intent(context, SubCategoryDetailsActivity.class);
//                intent.putExtra("cid", "4");
//                context.startActivity(intent);
//            } else {
//                Intent intent = new Intent(context, VendorDetailsActivityNew.class);
//                context.startActivity(intent);
//            }
//        });
//
//        try {
//            Picasso.get()
//                    .load((image_arraylist.get(position).getImage()))
//                    .placeholder(R.drawable.saloon1) // optional
//                    .error(R.drawable.saloon1)         // optional
//                    .into(image_banner);
//            container.addView(view);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return view;
//    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_offer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_offer_image = (ImageView)itemView.findViewById(R.id.banners_item_image);

        }
    }
}