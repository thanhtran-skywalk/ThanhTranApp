package com.thanhtran.redstring.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.thanhtran.redstring.R;
import com.thanhtran.redstring.main.adapter.TabPagerAdapter;
import com.parse.ParseUser;
import com.thanhtran.redstring.utils.Constants;


public class StoryActivity extends FragmentActivity {
    private LinearLayout view;
    private int oldTabSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser.logOut();
        if(ParseUser.getCurrentUser() ==null){// User has not logged in.
            startLoginActivity();
        }else{
            initView();
        }
    }

    private void initView(){
        this.setContentView(R.layout.main_activity_story);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        final PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);
        view = (LinearLayout) tabsStrip.getChildAt(0);
        setActiveIcon(view, 0);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setActiveIcon(view, position);
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

    private void setActiveIcon(LinearLayout view, int position) {
        view.getChildAt(oldTabSelected).setSelected(false);
        view.getChildAt(position).setSelected(true);
    }

    private void startLoginActivity(){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, Constants.LOGIN_REQUEST_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.LOGIN_REQUEST_CODE) {
            initView();
        }
    }

}