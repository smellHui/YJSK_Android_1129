package com.yangj.dahemodule.activity;

import android.support.v4.app.Fragment;

import com.tepia.base.mvp.BaseActivity;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.fragment.DangerReportListFragment;
import com.yangj.dahemodule.view.SearchToolBar;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class DangerReportListActivity extends BaseActivity {

    private Fragment fragment;
    private SearchToolBar searchToolBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_danger_report_list;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        searchToolBar = findViewById(R.id.view_search_tool_bar);
        fragment = DangerReportListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setStatusBarTextDark() {
        searchToolBar.setImmersionBar(getmImmersionBar());
        super.setStatusBarTextDark();
    }

    @Override
    protected void initRequestData() {

    }
}
