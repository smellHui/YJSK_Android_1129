package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.view.View;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.activity.DangerReportActivity;
import com.yangj.dahemodule.common.UserManager;
import com.yangj.dahemodule.model.main.MainBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.main.ReservoirInfo;
import com.yangj.dahemodule.view.BasicInfoView;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:首页
 */
public class HomeFragment extends BaseCommonFragment {

    private BasicInfoView basicInfoView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        basicInfoView = findView(R.id.view_basic_info);

        findView(R.id.btn_report).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DangerReportActivity.class));
        });
    }

    @Override
    protected void initRequestData() {
        loadData();
    }

    private void loadData(){
        UserManager.getInstance().loadData("")
                .subscribe(new LoadingSubject<MainDataBean>(){

                    @Override
                    protected void _onNext(MainDataBean mainDataBean) {
                        MainBean mainBean = mainDataBean.getData();
                        if (mainBean == null) return;
                        ReservoirInfo reservoirInfo = mainBean.getReservoirInfo();
                        basicInfoView.setData(reservoirInfo);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
