package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.view.View;

import com.tepia.base.mvp.BaseCommonFragment;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.activity.DangerReportActivity;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:首页
 */
public class HomeFragment extends BaseCommonFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        findView(R.id.btn_report).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DangerReportActivity.class));
        });
    }

    @Override
    protected void initRequestData() {

    }
}
