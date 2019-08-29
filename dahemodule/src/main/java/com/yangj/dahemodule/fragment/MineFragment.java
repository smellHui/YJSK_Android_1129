package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.view.View;

import com.tepia.base.mvp.BaseCommonFragment;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.activity.LoginActivity;
import com.yangj.dahemodule.activity.OperatesActivity;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的
 */
public class MineFragment extends BaseCommonFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        findView(R.id.ll_one).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), OperatesActivity.class));
        });
        findView(R.id.ll_four).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
        });
    }

    @Override
    protected void initRequestData() {

    }
}
