package com.yangj.dahemodule.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.xuncha.RecordBean;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class OperateAdapter extends BaseQuickAdapter<RecordBean, BaseViewHolder> {

    public OperateAdapter() {
        super(R.layout.item_operate);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean item) {
      helper.setText(R.id.tv_name,item.getName());
      helper.setText(R.id.tv_createTime,item.getCreateTime());

    }
}
