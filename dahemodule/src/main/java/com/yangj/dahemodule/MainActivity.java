package com.yangj.dahemodule;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.tepia.base.AppRoutePath;
import com.tepia.base.common.CommonFragmentPagerAdapter;
import com.tepia.base.common.HorizontalViewPager;
import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.fragment.HomeFragment;
import com.yangj.dahemodule.fragment.MineFragment;
import com.yangj.dahemodule.fragment.OperateFragment;

@Route(path = AppRoutePath.app_dahe_main)
public class MainActivity extends BaseActivity {

    private BottomNavigationBar mBottomNavigation;
    private HorizontalViewPager mViewPagers;
    private BottomNavigationItem mHomeFragment;
    private BottomNavigationItem mOperateFragment;
    private BottomNavigationItem mMineFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mViewPagers = findViewById(R.id.viewPagers);
        setViewPager();
        setBottomNavigation();
    }

    private void setViewPager() {
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new OperateFragment());
        adapter.addFragment(new MineFragment());
        mViewPagers.setAdapter(adapter);
        mViewPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigation.selectTab(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setBottomNavigation() {
        mBottomNavigation.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#0994ff")
                //默认未选择颜色
                .setInActiveColor("#a9adb4")
                //默认背景色
                .setBarBackgroundColor("#ffffff")
                .addItem(new BottomNavigationItem(R.mipmap.icon_home_blue, "首页").setInactiveIconResource(R.mipmap.icon_home_grey))
                .addItem(new BottomNavigationItem(R.mipmap.icon_patro_blue, "运维").setInactiveIconResource(R.mipmap.icon_patrol_gray))
                .addItem(new BottomNavigationItem(R.mipmap.icon_my_blue, "我的").setInactiveIconResource(R.mipmap.icon_my_gray))
                .setFirstSelectedPosition(0) //设置默认选中位置
                .initialise(); // 提交初始化（完成配置）

        mBottomNavigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPagers.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

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
