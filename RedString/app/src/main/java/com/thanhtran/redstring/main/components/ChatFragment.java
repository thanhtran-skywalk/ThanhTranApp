package com.thanhtran.redstring.main.components;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thanhtran.redstring.R;

/**
 * Created by ThanhTran on 9/28/2015.
 */
public class ChatFragment extends Fragment {
    public ChatFragment() {
        System.out.println("init ChatFragment..........");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_chat, container, false);
    }
}
