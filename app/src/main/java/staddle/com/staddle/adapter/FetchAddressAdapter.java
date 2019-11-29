package staddle.com.staddle.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.AllAddressActivity;
import staddle.com.staddle.activity.SchedulingActivity;
import staddle.com.staddle.bean.OfferAndPromoModel;
import staddle.com.staddle.bean.SavedAddressList;
import staddle.com.staddle.fragment.ShoppingFragment;

import static staddle.com.staddle.fragment.ShoppingFragment.AddressLayoutCheckout;
import static staddle.com.staddle.fragment.ShoppingFragment.REQUEST_CODETIME;

import static staddle.com.staddle.fragment.ShoppingFragment.paychckoutlayout;
import static staddle.com.staddle.fragment.ShoppingFragment.selectAddressShowLayout;
import static staddle.com.staddle.fragment.ShoppingFragment.serviceAddressNickNameT;
import static staddle.com.staddle.fragment.ShoppingFragment.serviceAddressT;

public class FetchAddressAdapter extends RecyclerView.Adapter<FetchAddressAdapter.MyViewHolder> {

    private Context context;
    private List<SavedAddressList> addresses;
    Activity activity;


    public FetchAddressAdapter(List<SavedAddressList> savedAddressLists, Context context) {
        this.context = context;
        this.addresses = savedAddressLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.selectaddresslistitem, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view); // pass the view to View Holder
        activity=(Activity)context;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.nickName.setText(addresses.get(position).getNickName());
        myViewHolder.addressString.setText(addresses.get(position).getAddressString());
        myViewHolder.addressItme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ShoppingFragment.cid.equals("4")) {
                    AddressLayoutCheckout.setVisibility(View.GONE);
                    paychckoutlayout.setVisibility(View.VISIBLE);
                    selectAddressShowLayout.setVisibility(View.VISIBLE);
                    serviceAddressT.setText(addresses.get(position).getAddressString());
                    serviceAddressNickNameT.setText(addresses.get(position).getNickName());
                    ShoppingFragment.serviceAddress = addresses.get(position).getAddressString();
                    ShoppingFragment.serviceAddressNickName = addresses.get(position).getNickName();
                }else{
                    Intent intent=new Intent(context, SchedulingActivity.class);
                    intent.putExtra("nickname",addresses.get(position).getNickName());
                    intent.putExtra("address",addresses.get(position).getAddressString());
                    intent.putExtra("atHome","0");
                    activity.startActivityForResult(intent, AllAddressActivity.GETSCHEDULEREQCODE);
                }
            }
        });

    }
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nickName;
        TextView addressString;
        RelativeLayout addressItme;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.addressNickName);
            addressString = itemView.findViewById(R.id.addressfielditem);
            addressItme=itemView.findViewById(R.id.addressItemLayout);

        }
    }
}
