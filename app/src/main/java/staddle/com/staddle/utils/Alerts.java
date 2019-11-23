package staddle.com.staddle.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import staddle.com.staddle.R;


public class Alerts {

    private static LayoutInflater mInflater;

    public static void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = mInflater.inflate(R.layout.custom_dialog_alert, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        TextView txt_ok = (TextView) dialogView.findViewById(R.id.txt_ok);
        TextView tv_msg_body = (TextView) dialogView.findViewById(R.id.tv_msg_body);

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
