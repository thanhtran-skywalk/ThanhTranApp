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
public class SettingFragment extends Fragment {
    public SettingFragment() {
        System.out.println("init SettingFragment..........");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting, container, false);
    }
}
