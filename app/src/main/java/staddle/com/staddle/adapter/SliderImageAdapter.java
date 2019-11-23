package staddle.com.staddle.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.AtHomeActivity;
import staddle.com.staddle.activity.HouseKeepingActivity;
import staddle.com.staddle.activity.SecurityGuardActivity;
import staddle.com.staddle.activity.SubCategoryDetailsActivity;
import staddle.com.staddle.activity.VendorDetailsActivityNew;
import staddle.com.staddle.bean.SliderImagesModule;

public class SliderImageAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Context context;
    private ArrayList<SliderImagesModule> image_arraylist;
    private String cid = "";

    public SliderImageAdapter(Context context, ArrayList<SliderImagesModule> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
        ImageView image_banner = view.findViewById(R.id.image_banner);
        SliderImagesModule sliderImagesModule = image_arraylist.get(position);
        String image = sliderImagesModule.getImage();

        image_banner.setOnClickListener(view1 -> {
            cid = sliderImagesModule.getCid();
            if (cid.equalsIgnoreCase("1")) {
                Intent intent = new Intent(context, AtHomeActivity.class);
                context.startActivity(intent);
            } else if (cid.equalsIgnoreCase("2")) {
                Intent intent = new Intent(context, HouseKeepingActivity.class);
                context.startActivity(intent);
            } else if (cid.equalsIgnoreCase("3")) {
                Intent intent = new Intent(context, SecurityGuardActivity.class);
                context.startActivity(intent);
            } else if (cid.equalsIgnoreCase("4")) {
                Intent intent = new Intent(context, SubCategoryDetailsActivity.class);
                intent.putExtra("cid", "4");
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, VendorDetailsActivityNew.class);
                context.startActivity(intent);
            }
        });

        try {
            Picasso.get()
                    .load((image_arraylist.get(position).getImage()))
                    .placeholder(R.drawable.saloon1) // optional
                    .error(R.drawable.saloon1)         // optional
                    .into(image_banner);
            container.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}