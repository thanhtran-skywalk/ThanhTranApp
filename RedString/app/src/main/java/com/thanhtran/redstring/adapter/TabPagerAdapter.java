package com.thanhtran.redstring.adapter;

/**
 * Created by ThanhTran on 9/28/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.thanhtran.redstring.R;
import com.thanhtran.redstring.fragment.ChatFragment;
import com.thanhtran.redstring.fragment.LoveChartFragment;
import com.thanhtran.redstring.fragment.SettingFragment;
import com.thanhtran.redstring.fragment.StoryFragment;


public class TabPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private int tabIcons[] = {R.drawable.time_line_selector, R.drawable.chat_selector, R.drawable.chart_selector, R.drawable.settings_selector};
    private static StoryFragment storyFragment;
    private static ChatFragment chatFragment;
    private static LoveChartFragment loveChartFragment;
    private static SettingFragment settingFragment;

    public static StoryFragment getStoryFragment() {
        if (storyFragment == null) {
            storyFragment = new StoryFragment();
        }
        return storyFragment;
    }

    public static ChatFragment getChatFragment() {
        if (chatFragment == null) {
            chatFragment = new ChatFragment();
        }
        return chatFragment;
    }

    public static LoveChartFragment getLoveChartFragment() {
        if (loveChartFragment == null) {
            loveChartFragment = new LoveChartFragment();
        }
        return loveChartFragment;
    }


    public static SettingFragment getSettingFragment() {
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        return settingFragment;
    }

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
                return getStoryFragment();
            case 1:
                return getChatFragment();
            case 2:
                return getLoveChartFragment();
            case 3:
                return getSettingFragment();
        }

        return null;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

}