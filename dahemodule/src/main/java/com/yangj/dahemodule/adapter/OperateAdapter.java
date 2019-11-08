package com.yangj.dahemodule.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.xuncha.RecordBean;

import static com.yangj.dahemodule.fragment.operate.OperatesFragment.ALL_OPERATE;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class OperateAdapter extends BaseQuickAdapter<RecordBean, BaseViewHolder> {

    private int pageType;

    public OperateAdapter(int pageType) {
        super(R.layout.item_operate);
        this.pageType = pageType;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean item) {

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_createTime, item.getCreateTime());

        int abnormalColor = mContext.getResources().getColor(item.isZeroAbnormal() ? R.color.color_1785F8 : R.color.color_FFA200);
        helper.setTextColor(R.id.tv_executeStatus, abnormalColor);
        helper.setTextColor(R.id.tv_abnormalNum, abnormalColor);
        helper.setText(R.id.tv_abnormalNum, item.getAbnormalNum());
        helper.setVisible(R.id.tv_creatorName, pageType == ALL_OPERATE);
        helper.setText(R.id.tv_creatorName, item.getCreatorName());

        String routeType = item.getRouteType();
        boolean isExecuting = item.isExecuting();
        helper.setText(R.id.tv_status, isExecuting ? "巡查中" : "已巡查");
        helper.setBackgroundRes(R.id.tv_status, isExecuting ? R.mipmap.blue : R.mipmap.grey);
    }
}
