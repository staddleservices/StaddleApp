package staddle.com.staddle.activity;

import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddle.com.staddle.R;
import staddle.com.staddle.ResponseClasses.VendorListResponse;
import staddle.com.staddle.adapter.VendorListNewAdapter;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.ApiClient;
import staddle.com.staddle.retrofitApi.ApiInterface;
import staddle.com.staddle.sheardPref.AppPreferences;

public class SearchServicesActivity extends AppCompatActivity {


    ImageView backbtn;
    EditText search_box;
    RecyclerView services_list;
    private ApiInterface apiInterface;
    ArrayList<VendorListModel> modelArrayList;
    private VendorListNewAdapter vendorListNewAdapter;
    private TextView txtNoMeassage;
    TextView show_search_key;
    TextView custom_tap_search;
    ProgressDialog progressDialog;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);
        init();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                    // TODO: do what you need here
                    // ............
                    // ............

                    vendorList(search_box.getText().toString());
                }
            }
        };

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(input_finish_checker);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>2){
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);


                }else{
                    search_box.setError("Type more");
                }

            }
        });
    }



    private void init() {

        search_box = findViewById(R.id.search_services_edt);
        services_list = findViewById(R.id.vendor_results);
        services_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        txtNoMeassage = findViewById(R.id.not_found_text);
        backbtn = findViewById(R.id.backbtn);
        custom_tap_search = findViewById(R.id.custom_search);
        show_search_key = findViewById(R.id.show_search_key);
        modelArrayList = new ArrayList<>();
    }

    private void vendorList(String query) {

        progressDialog = new ProgressDialog(SearchServicesActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<VendorListResponse> call = apiInterface.getVendorAccordingServices(query);

        call.enqueue(new Callback<VendorListResponse>() {
            @Override
            public void onResponse(@NonNull Call<VendorListResponse> call, @NonNull Response<VendorListResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        modelArrayList.clear();
                        VendorListResponse vendorListResponse = response.body();
                        if (vendorListResponse != null) {
                            String status = vendorListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                modelArrayList = vendorListResponse.getInfo();

                                if (modelArrayList != null) {
                                    vendorListNewAdapter = new VendorListNewAdapter(SearchServicesActivity.this, modelArrayList);
                                    vendorListNewAdapter.setHasStableIds(true);
                                    services_list.setHasFixedSize(true);
                                    services_list.setAdapter(vendorListNewAdapter);
                                } else {
                                    //Toast.makeText(mContext, "" + vendorListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                custom_tap_search.setVisibility(View.GONE);
                                services_list.setVisibility(View.VISIBLE);
                                show_search_key.setText("Vendors who provide '"+query+"'");
                                show_search_key.setVisibility(View.VISIBLE);
                                txtNoMeassage.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            } else {
                                txtNoMeassage.setVisibility(View.VISIBLE);
                                services_list.setVisibility(View.GONE);
                                show_search_key.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Response Fail !!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VendorListResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "server error"+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
