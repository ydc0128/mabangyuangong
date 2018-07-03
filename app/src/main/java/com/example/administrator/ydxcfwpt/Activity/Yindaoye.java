package com.example.administrator.ydxcfwpt.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;


import com.example.administrator.ydxcfwpt.Adapter.MyFragmentPagerAdapter;
import com.example.administrator.ydxcfwpt.R;
import com.example.administrator.ydxcfwpt.fragment.Yindaoye1Fragment;
import com.example.administrator.ydxcfwpt.fragment.Yindaoye2Fragment;
import com.example.administrator.ydxcfwpt.fragment.Yindaoye3Fragment;

import java.util.ArrayList;
import java.util.List;



public class Yindaoye extends BaseActivity {
//引导页
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_yindaoye);
        initViewPager();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vp_yindaoye_viewpager);
        List<Fragment> fragmentList = new ArrayList<>();
        Yindaoye1Fragment yindaoye1Fragment = new Yindaoye1Fragment();
        Yindaoye2Fragment yindaoye2Fragment = new Yindaoye2Fragment();
        Yindaoye3Fragment yindaoye3Fragment = new Yindaoye3Fragment();
        fragmentList.add(yindaoye1Fragment);
        fragmentList.add(yindaoye2Fragment);
        fragmentList.add(yindaoye3Fragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);
    }
}
