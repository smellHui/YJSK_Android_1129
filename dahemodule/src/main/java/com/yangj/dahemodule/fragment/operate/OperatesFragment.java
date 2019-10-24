package com.yangj.dahemodule.fragment.operate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.yangj.dahemodule.adapter.OperateAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.fragment.BaseListFragment;
import com.yangj.dahemodule.model.main.ReservoirStructure;
import com.yangj.dahemodule.model.xuncha.ProtalBean;
import com.yangj.dahemodule.model.xuncha.ProtalDataBean;
import com.yangj.dahemodule.model.xuncha.ProtalItemBean;
import com.yangj.dahemodule.model.xuncha.RecordBean;
import com.yangj.dahemodule.model.xuncha.RecordDataBean;
import com.yangj.dahemodule.util.UiHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的巡查
 */
public class OperatesFragment extends BaseListFragment<RecordBean> {

    public static final int MINE_OPERATE = 1;
    public static final int ALL_OPERATE = MINE_OPERATE >> 1;

    private int pageType;
    private String startTime, endTime;
    private String userCode;
    private List<TaskBean> toDoLocalTask;

    private List<RecordBean> localRecordList;

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

        localRecordList = new ArrayList<>();
        userCode = UserManager.getInstance().getUserCode();
    }

    @Override
    protected void initRequestData() {
        getRecordList();
    }

    private void getRecordList() {
        HttpManager.getInstance().getRecordList(pageType, "", getPage(), 20, startTime, endTime)
                .subscribe(new LoadingSubject<RecordDataBean>() {

                    @Override
                    protected void _onNext(RecordDataBean recordDataBean) {
                        addLocalData();
                        success(recordDataBean.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        addLocalData();
                        ToastUtils.shortToast(message);
                    }
                });
    }

    private void addLocalData() {
        localRecordList.clear();
        if (pageType == MINE_OPERATE) {
            toDoLocalTask = DataSupport.where("userCode=? and executeStatus != 3", userCode)
                    .order("id desc")
                    .find(TaskBean.class);
            if (!CollectionsUtil.isEmpty(toDoLocalTask)) {
                for (TaskBean taskBean : toDoLocalTask) {
                    RecordBean recordBean = new RecordBean();
                    recordBean.setName(taskBean.getRouteName());
                    recordBean.setCreateTime(taskBean.getStartTime());
                    recordBean.setCode(taskBean.getWorkOrderId());
                    localRecordList.add(recordBean);
                }
            }
            if (getPage() == 1 && !CollectionsUtil.isEmpty(localRecordList)) {
                getList().clear();
                getList().addAll(localRecordList);
            }
        }
    }

    public void refresh(String startTime, String endTime, int cate) {
        this.startTime = startTime;
        this.endTime = endTime;
        super.refresh();
    }

    @Override
    public BaseQuickAdapter getBaseQuickAdapter() {
        return new OperateAdapter();
    }

    @Override
    public void setOnItemClickListener(BaseQuickAdapter adapter, View view, int position) {
        RecordBean recordBean = (RecordBean) adapter.getItem(position);
        if (recordBean == null) return;
        touchTaskBean = queryTaskBeanBySql(recordBean.getCode());
        if (touchTaskBean != null) {
            UiHelper.goToStartInspectionView(getBaseActivity(), touchTaskBean.getWorkOrderId());
        } else {
            loadPatrolDetail(recordBean.getCode());
        }
    }

    private TaskBean touchTaskBean;

    private TaskBean queryTaskBeanBySql(String workOrderId) {
        return DataSupport.where("workorderid=?", workOrderId).findFirst(TaskBean.class);
    }

    private void loadPatrolDetail(String omRecordCode) {
        HttpManager.getInstance().getPatrolDetail(omRecordCode)
                .subscribe(new LoadingSubject<ProtalDataBean>() {

                    @Override
                    protected void _onNext(ProtalDataBean reportDataBean) {
                        if (reportDataBean == null) return;
                        LogUtil.i(reportDataBean.toString());
                        doProtalDetail(reportDataBean.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.shortToast(message);
                    }
                });
    }

    private void doProtalDetail(ProtalBean protalBean) {
        if (protalBean == null) return;
        String workOrderId = protalBean.getCode();
        List<ProtalItemBean> itemList = protalBean.getItemList();
        List<ReservoirStructure> reservoirStructureList = protalBean.getReservoirStructureList();
        ReservoirBean selectedResrvoir = UserManager.getInstance().getDefaultReservoir();
        if (selectedResrvoir == null) {
            ToastUtils.shortToast("暂无水库信息");
            return;
        }
        if (touchTaskBean == null) {
            String userCode = protalBean.getCreatorName();
            //本地不存在工单详情数据，则保存
            TaskBean taskBean = new TaskBean();
            taskBean.setWorkOrderId(workOrderId);
            taskBean.setRouteId(protalBean.getOmRouteId());
            taskBean.setReservoirId(selectedResrvoir.getReservoirId());
            taskBean.setReservoir(selectedResrvoir.getReservoir());
            taskBean.setUserCode(userCode);
            taskBean.setRoleName(protalBean.getCreatorName());
            taskBean.setExecutorName(protalBean.getCreatorName());
            taskBean.setStartTime(protalBean.getCreateTime());
            taskBean.setExecuteStatus("3");
            //工单名字
            taskBean.setRouteName(protalBean.getName());
            taskBean.setWorkOrderRoute(protalBean.getRoutePath());
            taskBean.save();

            List<TaskItemBean> taskItemBeanList = new ArrayList<>();
            if (!CollectionsUtil.isEmpty(itemList)) {
                for (ProtalItemBean protalItemBean : itemList) {
                    TaskItemBean taskItem = new TaskItemBean();
                    taskItem.setItemId(protalItemBean.getOmItemId());
                    taskItem.setWorkOrderId(workOrderId);
                    taskItem.setUserCode(userCode);
                    taskItem.setPositionTreeNames(protalItemBean.getPositionName());
                    taskItem.setPositionName(protalItemBean.getPositionName());
                    taskItem.setSuperviseItemName(protalItemBean.getOmItemName());
                    taskItem.setSuperviseItemContent(protalItemBean.getOmItemContent());
                    taskItem.setPositionLatitude(protalItemBean.getPositionLatitude());
                    taskItem.setPositionLongitude(protalItemBean.getPositionLongitude());
                    taskItem.setReservoirId(selectedResrvoir.getReservoirId());
                    taskItem.setPositionId(protalItemBean.getReservoirStructureId());
                    taskItem.setContent(protalItemBean.getOmItemContent());
                    taskItem.setResultType(protalItemBean.getOmItemResultType());
                    taskItem.setExecuteResultType(protalItemBean.getExecuteResultType());
                    taskItem.setExcuteDate(protalItemBean.getExecuteTime());
                    taskItem.setExcuteLatitude(protalItemBean.getExecuteLatitude());
                    taskItem.setExcuteLongitude(protalItemBean.getExecuteLongitude());
                    taskItem.setExecuteResultDescription(protalItemBean.getExecuteResultDescription());
                    taskItem.setLastExcuteResultType(protalItemBean.getLastExecuteResultType());
                    String pictures = protalItemBean.getPictures();
                    if (!TextUtils.isEmpty(pictures)) {
                        String[] paths = pictures.split(",");
                        List<TaskItemBean.ISysFileUploadsBean> iSysFileUploadsBeans = new ArrayList<>();
                        for (String path : paths) {
                            TaskItemBean.ISysFileUploadsBean fileUploadsBean = new TaskItemBean.ISysFileUploadsBean();
                            fileUploadsBean.setFilePath(path);
                            iSysFileUploadsBeans.add(fileUploadsBean);
                        }
                        taskItem.setiSysFileUploads(iSysFileUploadsBeans);
                    }
                    taskItemBeanList.add(taskItem);
                }
            }

            List<RoutePosition> routePositions = new ArrayList<>();
            if (!CollectionsUtil.isEmpty(reservoirStructureList)) {
                for (ReservoirStructure reservoirStructure : reservoirStructureList) {
                    RoutePosition routePosition = new RoutePosition();
                    routePosition.setWorkOrderId(workOrderId);
                    routePosition.setUserCode(userCode);
                    routePosition.setReservoirId(selectedResrvoir.getReservoirId());
                    routePosition.setPositionId(reservoirStructure.getId());
                    routePosition.setPositionLgtd(reservoirStructure.getPositionLongitude());
                    routePosition.setPositionLttd(reservoirStructure.getPositionLatitude());
                    routePositions.add(routePosition);
                }
            }
            RouteListBean routeListBeanNew = new RouteListBean();
            routeListBeanNew.setWorkOrderId(workOrderId);
            routeListBeanNew.setId(protalBean.getOmRouteId());
            routeListBeanNew.setRouteContent(protalBean.getRoutePath());
            routeListBeanNew.setUserCode(userCode);
            routeListBeanNew.setReservoirId(selectedResrvoir.getReservoirId());
            routeListBeanNew.setItemList(taskItemBeanList);
            routeListBeanNew.setRouteName(protalBean.getName());
            routeListBeanNew.setRoutePositions(routePositions);
//            routeListBeanNew.setRouteType();
            DataSupport.saveAll(taskItemBeanList);
            DataSupport.saveAll(routePositions);
            boolean save = routeListBeanNew.save();

            RouteListBean routeListBean_a = taskBean.getRouteListBeanByWorkId(workOrderId);

            if (save && routeListBean_a != null) {
//                MainFragment.saveOther(workOrderId, routeListBeanNew, "1", userCodeOfnet);
//                goStart(workOrderId);
                UiHelper.goToStartInspectionView(getBaseActivity(), workOrderId);
            }
        }
    }
}
