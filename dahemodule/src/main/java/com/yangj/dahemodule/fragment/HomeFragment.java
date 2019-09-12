package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.activity.DangerReportActivity;
import com.yangj.dahemodule.common.UserManager;
import com.yangj.dahemodule.model.main.DangerousPosition;
import com.yangj.dahemodule.model.main.MainBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.main.ReservoirInfo;
import com.yangj.dahemodule.model.main.Route;
import com.yangj.dahemodule.model.main.UserInfo;
import com.yangj.dahemodule.view.BasicInfoView;

import java.util.List;

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

    private void loadData() {
        UserManager.getInstance().loadData("")
                .subscribe(new LoadingSubject<MainDataBean>() {

                    @Override
                    protected void _onNext(MainDataBean mainDataBean) {
                        MainBean mainBean = mainDataBean.getData();
                        if (mainBean == null) return;
                        List<Route> routeList = mainBean.getRouteList();
                        if (!CollectionsUtil.isEmpty(routeList)) {
                            UserManager.getInstance().saveRoutes(JSON.toJSONString(routeList));
                        }
                        List<UserInfo> userInfos = mainBean.getUserList();
                        if (!CollectionsUtil.isEmpty(userInfos)) {
                            UserManager.getInstance().saveUserInfos(JSON.toJSONString(userInfos));
                        }
                        List<DangerousPosition> dangerousPositions = mainBean.getDangerousPositionList();
                        if (!CollectionsUtil.isEmpty(dangerousPositions)) {
                            UserManager.getInstance().saveDangerousPositions(JSON.toJSONString(dangerousPositions));
                        }
                        ReservoirInfo reservoirInfo = mainBean.getReservoirInfo();
                        if (reservoirInfo != null) {
                            ReservoirBean reservoirBean = new ReservoirBean();
                            reservoirBean.setReservoirId(reservoirInfo.getId());
                            reservoirBean.setReservoirCode(reservoirInfo.getCode());
                            reservoirBean.setReservoir(reservoirInfo.getName());
                            reservoirBean.setReservoirType(reservoirInfo.getType());
                            reservoirBean.setReservoirAddress(reservoirInfo.getAddress());
                            reservoirBean.setReservoirLongitude(reservoirInfo.getLongitude());
                            reservoirBean.setReservoirLatitude(reservoirInfo.getLatitude());
                            reservoirBean.setManageDepId(reservoirInfo.getManageDepartment());
                            UserManager.getInstance().saveDefaultReservoir(reservoirBean);
                            basicInfoView.setData(reservoirInfo);
                        }
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
