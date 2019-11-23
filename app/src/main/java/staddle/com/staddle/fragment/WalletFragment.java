package staddle.com.staddle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import staddle.com.staddle.R;

public class WalletFragment extends Fragment {

    View view;
    Context mContext;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wallet_fragment, container, false);
        mContext = getActivity();

        find_All_IDs(view);
        return view;
    }

    private void find_All_IDs(View view) {
        //  rlv_house_keeping = view.findViewById(R.id.rlv_house_keeping);

    }

}