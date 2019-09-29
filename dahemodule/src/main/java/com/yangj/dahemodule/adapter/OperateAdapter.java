package com.yangj.dahemodule.adapter;

import android.text.TextUtils;
import android.widget.TextView;

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
        TextView statusTv = helper.getView(R.id.tv_status);
        String executeStatus = item.getExecuteStatus();
        if (!TextUtils.isEmpty(executeStatus)){
            statusTv.setText(executeStatus.equals("1") ? "巡查中" : "已巡查");
            statusTv.setBackgroundResource(executeStatus.equals("1") ? R.mipmap.blue : R.mipmap.green);
        }
      helper.setText(R.id.tv_name,item.getName());
      helper.setText(R.id.tv_createTime,item.getCreateTime());

    }
}
