package com.yangj.dahemodule.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tepia.base.common.CommonFragmentPagerAdapter;
import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.OperatesFragment;
import com.yangj.dahemodule.model.TabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:
 */
public class OperatesActivity extends BaseActivity {
    public final static int REPORT_SITE = 2;//我的巡查
    public final static int FAULT_SITE = REPORT_SITE >> 1;//全部巡查
    private String[] mTitles_2 = {"我的巡查", "全部巡查"};
    private ArrayList<CustomTabEntity> tabEntities;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private CommonTabLayout mTabLayout;
    private ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_operates;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mTabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.vp);

        mFragments.add(OperatesFragment.launch(0));
        mFragments.add(OperatesFragment.launch(1));
        tabEntities = new ArrayList<>();
        tabEntities.add(new TabEntity("我的巡查"));
        tabEntities.add(new TabEntity("全部巡查"));
        mTabLayout.setTabData(tabEntities);
        viewPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTabLayout.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
