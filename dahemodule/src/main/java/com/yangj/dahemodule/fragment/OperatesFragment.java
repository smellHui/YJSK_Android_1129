package com.yangj.dahemodule.fragment;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ToastUtils;
import com.yangj.dahemodule.adapter.OperateAdapter;
import com.yangj.dahemodule.common.UserManager;
import com.yangj.dahemodule.model.xuncha.RecordBean;
import com.yangj.dahemodule.model.xuncha.RecordDataBean;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的巡查
 */
public class OperatesFragment extends BaseListFragment<RecordBean> {

    public static final int MINE_OPERATE = 1;
    public static final int ALL_OPERATE = MINE_OPERATE >> 1;

    private int pageType;

    public static OperatesFragment launch(int pageType) {
        OperatesFragment operatesFragment = new OperatesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageType", pageType);
        operatesFragment.setArguments(bundle);
        return operatesFragment;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt("pageType", MINE_OPERATE);
        }
    }

    @Override
    protected void initRequestData() {
        UserManager.getInstance().getRecordList(pageType, "", getPage(), 20, "", "")
                .subscribe(new LoadingSubject<RecordDataBean>() {

                    @Override
                    protected void _onNext(RecordDataBean recordDataBean) {
                        success(recordDataBean.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.shortToast(message);
                    }
                });
    }

    @Override
    public BaseQuickAdapter getBaseQuickAdapter() {
        return new OperateAdapter();
    }
}
