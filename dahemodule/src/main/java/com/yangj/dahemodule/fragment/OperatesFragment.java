package com.yangj.dahemodule.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yangj.dahemodule.adapter.OperateAdapter;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的巡查
 */
public class OperatesFragment extends BaseListFragment<String> {

    public static OperatesFragment launch(int pageType) {
        OperatesFragment operatesFragment = new OperatesFragment();
        return operatesFragment;
    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public BaseQuickAdapter getBaseQuickAdapter() {
        return new OperateAdapter();
    }
}
