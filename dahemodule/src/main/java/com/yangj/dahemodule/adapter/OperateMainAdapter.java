package com.yangj.dahemodule.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.main.Route;

import java.util.List;

/**
 * Author:xch
 * Date:2019/9/18
 * Description:
 */
public class OperateMainAdapter extends BaseQuickAdapter<Route, BaseViewHolder> {

    public OperateMainAdapter(@Nullable List<Route> data) {
        super(R.layout.item_operate_main, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Route item) {
        helper.setText(R.id.tv_name, item.getName());
    }
}
