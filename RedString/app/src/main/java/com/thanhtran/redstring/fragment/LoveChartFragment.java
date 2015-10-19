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
public class LoveChartFragment extends Fragment {
    public LoveChartFragment() {
        System.out.println("init LoveChartFragment..........");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.love_chart, container, false);
    }
}
