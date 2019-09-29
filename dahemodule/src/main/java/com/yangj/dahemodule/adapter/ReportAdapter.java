package com.yangj.dahemodule.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.Report.ReportBean;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class ReportAdapter extends BaseQuickAdapter<ReportBean, BaseViewHolder> {

    public ReportAdapter() {
        super(R.layout.item_report);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportBean item) {
        TextView statusTv = helper.getView(R.id.tv_status);
        String problemStatus = item.getProblemStatus();
        if (!TextUtils.isEmpty(problemStatus)) {
            statusTv.setText(problemStatus.equals("1") ? "待反馈" : "已完结");
            statusTv.setBackgroundResource(problemStatus.equals("1") ? R.mipmap.blue : R.mipmap.green);
        }
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_createTime, item.getCreateTime());
    }
}
