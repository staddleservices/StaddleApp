package staddle.com.staddle.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import staddle.com.staddle.R;
import staddle.com.staddle.adapter.VendorListNewAdapter;
import staddle.com.staddle.bean.VendorListModel;
import staddle.com.staddle.retrofitApi.ApiInterface;

public class BeautyFragment extends Fragment {
    Context mContext;
    RecyclerView  productlist_recyclerview;
    ApiInterface apiInterface;
    //vendor list
    ArrayList<VendorListModel> vendorListModelArrayList;
    ProgressDialog progressDialog;
    TextView txtNoMeassage;

    //
    VendorListNewAdapter vendorListNewAdapter;

    public BeautyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beauty_fragment, container, false);
        mContext = getActivity();
        vendorListModelArrayList = new ArrayList<>();

        find_All_IDs(view);
        
        return view;
    }

    private void find_All_IDs(View view) {
        productlist_recyclerview = view.findViewById(R.id.productlist_recyclerview);
        txtNoMeassage = view.findViewById(R.id.txtNoMeassage);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        productlist_recyclerview.setLayoutManager(staggeredGridLayoutManager);
        productlist_recyclerview.setHasFixedSize(true);
        
    }
}
