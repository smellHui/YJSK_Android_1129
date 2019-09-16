package com.yangj.dahemodule.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ToastUtils;
import com.yangj.dahemodule.adapter.ReportAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.Report.ReportBean;
import com.yangj.dahemodule.model.Report.ReportDataBean;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class DangerReportListFragment extends BaseListFragment<ReportBean> {

    public static DangerReportListFragment newInstance() {
        DangerReportListFragment fragment = new DangerReportListFragment();
        return fragment;
    }

    @Override
    protected void initRequestData() {
        HttpManager.getInstance().getReportList("", getPage(), 20, "", "")
                .subscribe(new LoadingSubject<ReportDataBean>() {

                    @Override
                    protected void _onNext(ReportDataBean recordDataBean) {
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
        return new ReportAdapter();
    }
}
