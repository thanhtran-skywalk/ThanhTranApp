package com.thanhtran.redstring.fragment;

/**
 * Created by ThanhTran on 9/28/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import redstring.thanhtran.com.redstring.R;

public class TabPagerAdapter extends FragmentStatePagerAdapter  implements PagerSlidingTabStrip.IconTabProvider {
    private int tabIcons[] = {R.drawable.time_line_selector, R.drawable.chat_selector, R.drawable.chart_selector,R.drawable.calendar_selector, R.drawable.settings_selector};

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabIcons.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TimeLineFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new LoveChartFragment();
            case 3:
                return new CalendarFragment();
            case 4:
                return new SettingFragment();
        }

        return null;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

}