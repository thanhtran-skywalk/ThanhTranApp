package com.thanhtran.redstring.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import com.astuetz.PagerSlidingTabStrip;
import com.thanhtran.redstring.R;
import com.thanhtran.redstring.fragment.TabPagerAdapter;


public class MainActivity extends FragmentActivity {
    private LinearLayout view;
    private int oldTabSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        final PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
        view = (LinearLayout) tabsStrip.getChildAt(0);
        setActivedIcon(view, 0);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setActivedIcon(view, position);
                oldTabSelected = position;
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    private void setActivedIcon(LinearLayout view, int position) {
        view.getChildAt(oldTabSelected).setSelected(false);
        view.getChildAt(position).setSelected(true);
    }

}