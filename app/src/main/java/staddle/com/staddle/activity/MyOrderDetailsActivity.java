package staddle.com.staddle.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.adapter.MyOrderDetailsAdapter;
import staddle.com.staddle.adapter.MyOrderListAdapter;
import staddle.com.staddle.bean.MyOrderListModel;
import staddle.com.staddle.utils.AppConstants;

public class MyOrderDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<MyOrderListModel.Data> dataArrayList;
    MyOrderDetailsAdapter myOrderListAdapter;

    ImageView imgBack;
    TextView txtName, txtDate, txtOrderPrice, txtStatus, txtDiscPrice, txtDiscount,tv_item_total,txt_percentage,txt_overallTotalprice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);

        AppConstants.ChangeStatusBarColor(MyOrderDetailsActivity.this);

        setUpView();

        Intent intent = getIntent();
        if (intent != null) {
            dataArrayList = intent.getParcelableArrayListExtra("DATA_LIST");
            txtName.setText(intent.getStringExtra("NAME"));
            txtDate.setText(intent.getStringExtra("DATE"));
            tv_item_total.setText("₹ "+intent.getStringExtra("ORDER_PRICE"));
            txt_overallTotalprice.setText("₹ "+intent.getStringExtra("DICS_PRICE"));
            txt_percentage.setText("₹ "+intent.getStringExtra("DICSCOUNT"));

            if (intent.getStringExtra("ORDER_STATUS").equalsIgnoreCase("A"))
                txtStatus.setText("Accepted");
            else if (intent.getStringExtra("ORDER_STATUS").equalsIgnoreCase("C"))
                txtStatus.setText("Completed");
            else if (intent.getStringExtra("ORDER_STATUS").equalsIgnoreCase("P"))
                txtStatus.setText("Pending");
            else if (intent.getStringExtra("ORDER_STATUS").equalsIgnoreCase("R"))
                txtStatus.setText("Rejected");

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            myOrderListAdapter = new MyOrderDetailsAdapter(this, dataArrayList);
            recyclerView.setAdapter(myOrderListAdapter);
            recyclerView.setHasFixedSize(true);
            myOrderListAdapter.notifyDataSetChanged();
        }

        imgBack.setOnClickListener(view -> onBackPressed());
    }

    private void setUpView() {
        dataArrayList = new ArrayList<>();
        imgBack = findViewById(R.id.imgBack);
        txtName = findViewById(R.id.txtName);
        txtDate = findViewById(R.id.txtDate);
        txtOrderPrice = findViewById(R.id.txtOrderPrice);
        txtDiscPrice = findViewById(R.id.txtDiscPrice);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtStatus = findViewById(R.id.txtStatus);
        recyclerView = findViewById(R.id.recyclerView);
        tv_item_total = findViewById(R.id.tv_item_total);
        txt_percentage = findViewById(R.id.txt_percentage);
        txt_overallTotalprice = findViewById(R.id.txt_overallTotalprice);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
