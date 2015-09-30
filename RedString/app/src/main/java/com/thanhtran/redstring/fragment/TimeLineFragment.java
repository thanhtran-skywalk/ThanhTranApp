package com.thanhtran.redstring.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.thanhtran.redstring.R;


/**
 * Created by ThanhTran on 9/28/2015.
 */
public class TimeLineFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.time_line, container, false);
        return android;
    }
}
