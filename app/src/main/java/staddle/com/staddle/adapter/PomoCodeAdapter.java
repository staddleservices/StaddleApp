package staddle.com.staddle.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import staddle.com.staddle.R;
import staddle.com.staddle.activity.PromoCodeActivity;
import staddle.com.staddle.bean.PromoList;
import staddle.com.staddle.fragment.ShoppingFragment;

import static com.facebook.FacebookSdk.getApplicationContext;
import static staddle.com.staddle.activity.PromoCodeActivity.quantAlert;
import static staddle.com.staddle.fragment.ShoppingFragment.appliedpomodes;
import static staddle.com.staddle.fragment.ShoppingFragment.appliedpromovalue;

public class PomoCodeAdapter extends RecyclerView.Adapter<PomoCodeAdapter.ViewHolder>{
    private List<PromoList> listdata;

    Activity contextt;
    ProgressDialog progressDialog;

    // RecyclerView recyclerView;
    public PomoCodeAdapter(List<PromoList>  listdata,Activity context) {
        this.listdata = listdata;
        contextt=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.promolistitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.description.setText(listdata.get(position).getDescription());
       holder.promoname.setText(listdata.get(position).getPromoname());
       holder.applycode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               progressDialog=new ProgressDialog(contextt);
               progressDialog.setCancelable(false);
               progressDialog.setMessage("Loading...");
               progressDialog.show();
               PromoCodeActivity.promovalue=listdata.get(position).getPromovalue();
                CreateDialogBox( PromoCodeActivity.promovalue,listdata.get(position).getPromoname());
           }
       });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView applycode;
        TextView promoname;
        public ViewHolder(View itemView) {
            super(itemView);
            description=itemView.findViewById(R.id.promocontent);
            applycode=itemView.findViewById(R.id.applybtncoupon);
            promoname=itemView.findViewById(R.id.promonamecode);


        }
    }

    public  void CreateDialogBox(String promovalue, String promoname) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.promoappliedbox,
                null, false);
        final TextView couponname=(TextView)formElementsView.findViewById(R.id.couponnamebox);
        final TextView ttldiscountam=(TextView)formElementsView.findViewById(R.id.ttldiscountam);
        final TextView descdiscountam=(TextView)formElementsView.findViewById(R.id.descdiscountam);
        final  TextView okaywesometext=(TextView)formElementsView.findViewById(R.id.okaywesometext);

        couponname.setText(promoname);

        PromoCodeActivity.discount=((Float.valueOf(PromoCodeActivity.totalprice)*Float.valueOf(promovalue)/100));
        Log.e("TotalDiscount",""+PromoCodeActivity.discount);
        ttldiscountam.setText(PromoCodeActivity.discount+"");
        descdiscountam.setText("You have availed total discount of ₹"+PromoCodeActivity.discount);

        okaywesometext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantAlert.dismiss();
                appliedpomodes = promoname;
                ShoppingFragment.couponlayoutapplied.setVisibility(View.VISIBLE);
                ShoppingFragment.couponlayout.setVisibility(View.GONE);
                ShoppingFragment.appliedcoupontag.setText(promoname);
                Intent intent = new Intent();
                intent.putExtra(ShoppingFragment.DISCOUNTKEY, PromoCodeActivity.discount+"");
                intent.putExtra("promovalue",promovalue);

                contextt.setResult(ShoppingFragment.RESULT_CODE, intent); // You can also send result without any data using setResult(int resultCode)
                contextt.finish();

            }
        });


        quantAlert=new AlertDialog.Builder(contextt).setView(formElementsView)
                .setCancelable(false)
                .show();
        quantAlert.getWindow().getAttributes().windowAnimations = R.anim.zoom_out;
        quantAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog.dismiss();
    }

}