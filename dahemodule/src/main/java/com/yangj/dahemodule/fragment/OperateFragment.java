package com.yangj.dahemodule.fragment;

import android.view.View;

import com.tepia.base.CacheConsts;
import com.tepia.base.ConfigConsts;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.utils.UUIDUtil;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.MainFragment;
import com.tepia.photo_picker.entity.CheckTaskPicturesBean;
import com.tepia.photo_picker.utils.SPUtils;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.main.Omltem;
import com.yangj.dahemodule.model.main.ReservoirStructure;
import com.yangj.dahemodule.model.main.Route;
import com.yangj.dahemodule.util.UiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:运维
 */
public class OperateFragment extends BaseCommonFragment {

    private ReservoirBean reservoirBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_operate;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        findView(R.id.ll_one).setOnClickListener(v -> {
            createWorkOrder();
        });
        findView(R.id.ll_two).setOnClickListener(v -> {
            createWorkOrder();
        });
        findView(R.id.ll_three).setOnClickListener(v -> {
            createWorkOrder();
        });
    }

    @Override
    protected void initRequestData() {

    }

    private void createWorkOrder() {
        String workOrderId = UUIDUtil.getUUID32();
        reservoirBean = UserManager.getInstance().getDefaultReservoir();
        String userCode = UserManager.getInstance().getUserCode();
        if (reservoirBean != null) {
            SPUtils.getInstance().putString(CacheConsts.reservoirId, reservoirBean.getReservoirId());
            List<Route> routes = HttpManager.getInstance().getRoutes();
            if (!CollectionsUtil.isEmpty(routes)) {
                Route route = routes.get(0);
                if (route == null) {
                    ToastUtils.shortToast("暂无巡查路线");
                    return;
                }
                List<RoutePosition> routePositions = new ArrayList<>();
                List<ReservoirStructure> reservoirStructures = route.getReservoirStructureList();
                if (!CollectionsUtil.isEmpty(reservoirStructures)) {
                    for (ReservoirStructure reservoirStructure : reservoirStructures) {
                        RoutePosition routePosition = new RoutePosition();
                        routePosition.setWorkOrderId(workOrderId);
                        routePosition.setUserCode(userCode);
                        routePosition.setReservoirId(reservoirBean.getReservoirId());
                        routePosition.setPositionId(reservoirStructure.getId());
                        routePosition.setPositionLgtd(reservoirStructure.getPositionLongitude());
                        routePosition.setPositionLttd(reservoirStructure.getPositionLatitude());
                        routePositions.add(routePosition);
                    }
                }
                List<TaskItemBean> itemList = new ArrayList<>();
                List<Omltem> omltems = route.getOmItemList();
                if (!CollectionsUtil.isEmpty(omltems)) {
                    for (Omltem omltem : omltems) {
                        TaskItemBean taskItemBean = new TaskItemBean();
                        taskItemBean.setWorkOrderId(workOrderId);
                        taskItemBean.setUserCode(userCode);
                        taskItemBean.setItemId(omltem.getOmItemId());
                        taskItemBean.setPositionId(omltem.getReservoirStructureId());
                        taskItemBean.setPositionTreeNames(omltem.getPositionName());
                        taskItemBean.setSuperviseItemContent(omltem.getOmItemContent());
                        taskItemBean.setSuperviseItemCode(UUIDUtil.getUUID32());
                        taskItemBean.setOperationLevel(omltem.getOmItemLevel());
                        taskItemBean.setReservoirId(reservoirBean.getReservoirId());
                        taskItemBean.setSuperviseItemName(omltem.getOmItemName());
                        itemList.add(taskItemBean);
                    }
                }
                String startTime = TimeFormatUtils.getStringDate();
                RouteListBean routeListBeanNew = new RouteListBean();
                routeListBeanNew.setWorkOrderId(workOrderId);
                routeListBeanNew.setId(route.getId());
                routeListBeanNew.setRouteContent(route.getPath());
//                routeListBeanNew.setUserCode(userCode);
                routeListBeanNew.setReservoirId(reservoirBean.getReservoirId());
                routeListBeanNew.setItemList(itemList);
                routeListBeanNew.setRouteName(route.getName());
                routeListBeanNew.setRoutePositions(routePositions);
                routeListBeanNew.setRouteType(route.getType());
                boolean save = routeListBeanNew.save();

                TaskBean taskBean = new TaskBean();
                taskBean.setWorkOrderId(workOrderId);
                taskBean.setRouteId(routeListBeanNew.getId());
                taskBean.setReservoirId(reservoirBean.getReservoirId());
                taskBean.setReservoir(reservoirBean.getReservoir());
                taskBean.setUserCode(userCode);
                UserBean userBean = HttpManager.getInstance().getUser();
//            taskBean.setRoleName(userInfoBean.getData().getOfficeName());
                if (userBean != null) {
                    taskBean.setExecutorName(userBean.getAccess_token());
                }
                taskBean.setStartTime(startTime);
                taskBean.setExecuteStatus("2");
                taskBean.setRouteName(routeListBeanNew.getRouteName());
                RouteListBean routeListBean_a = taskBean.getRouteListBeanByWorkId(workOrderId);
                if (save && routeListBean_a != null) {
                    saveOther(workOrderId, routeListBeanNew, "0", userCode);
                    boolean issave = taskBean.save();
                    if (issave) {
                        UiHelper.goToStartInspectionView(getBaseActivity(), workOrderId);
                    }
                }
//                UiHelper.goToStartInspectionView(getBaseActivity(), workOrderId);

            }
        }
    }

    /**
     * 保存
     *
     * @param workOrderId
     * @param routeListBean
     */
    public static void saveOther(String workOrderId, RouteListBean routeListBean, String completeStatus, String userCode) {
        LogUtil.i(MainFragment.class.getName(), "--------------工单workOrderId:" + workOrderId);
        int num = 0;
        int numOFTaskItemBean = 0;
        String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
        for (TaskItemBean taskItemBean1 : routeListBean.getItemList()) {
            TaskItemBean taskItemBean = new TaskItemBean();
            taskItemBean.setPositionId(taskItemBean1.getPositionId());
            taskItemBean.setSuperviseItemCode(taskItemBean1.getSuperviseItemCode());
            taskItemBean.setPositionTreeNames(taskItemBean1.getPositionTreeNames());
            taskItemBean.setPositionName(taskItemBean1.getPositionName());
            taskItemBean.setSuperviseItemName(taskItemBean1.getSuperviseItemName());
            taskItemBean.setSuperviseItemContent(taskItemBean1.getSuperviseItemContent());
            taskItemBean.setLastExcuteResultType(taskItemBean1.getLastExcuteResultType());
            taskItemBean.setExcuteLongitude(taskItemBean1.getExcuteLongitude());
            taskItemBean.setExcuteLatitude(taskItemBean1.getExcuteLatitude());
            taskItemBean.setBeforelist(taskItemBean1.getBeforelist());
            taskItemBean.setExcuteDate(taskItemBean1.getExcuteDate());
            taskItemBean.setWorkOrderId(workOrderId);
            taskItemBean.setContent(taskItemBean1.getContent());
            taskItemBean.setSuperviseItemResultType(taskItemBean1.getSuperviseItemResultType());
            taskItemBean.setResultType(taskItemBean1.getResultType());
            taskItemBean.setPositionLatitude(taskItemBean1.getPositionLatitude());
            taskItemBean.setPositionLongitude(taskItemBean1.getPositionLongitude());
            taskItemBean.setOperationLevel(taskItemBean1.getOperationLevel());


//            String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
            taskItemBean.setUserCode(userCode);
            taskItemBean.setReservoirId(reservoirId);
            taskItemBean.setFatherId(routeListBean.getId());
            taskItemBean.setIsCommitLocal(completeStatus);
            taskItemBean.setItemId(taskItemBean1.getItemId());
            taskItemBean.setWorkOrderId(workOrderId);
            taskItemBean.setCompleteStatus(completeStatus);

            if ("1".equals(completeStatus)) {
                taskItemBean.setExecuteResultType(taskItemBean1.getExecuteResultType());
                taskItemBean.setExecuteResultDescription(taskItemBean1.getExecuteResultDescription());
                taskItemBean.setLastExcuteResultType(taskItemBean1.getLastExcuteResultType());
                taskItemBean.setLastExecuteResultDescription(taskItemBean1.getLastExecuteResultDescription());
                taskItemBean.setLastExcuteDate(taskItemBean1.getLastExcuteDate());
                List<TaskItemBean.ISysFileUploadsBean> iSysFileUploadsBeanList = taskItemBean1.getiSysFileUploads();
                if (!CollectionsUtil.isEmpty(iSysFileUploadsBeanList)) {
                    for (TaskItemBean.ISysFileUploadsBean iSysFileUploadsBean : iSysFileUploadsBeanList) {
                        CheckTaskPicturesBean checkTaskPicturesBean = new CheckTaskPicturesBean();
                        checkTaskPicturesBean.setFilePath(iSysFileUploadsBean.getFilePath());
                        LogUtil.e(MainFragment.class.getName(), "---------图片路径：" + iSysFileUploadsBean.getFilePath());
                        checkTaskPicturesBean.setHasCheck("1");
                        checkTaskPicturesBean.setReservoirId(reservoirId);
                        checkTaskPicturesBean.setUserCode(userCode);
                        checkTaskPicturesBean.setBizType(ConfigConsts.picType_trouble);
                        checkTaskPicturesBean.setItemId(taskItemBean.getItemId());
                        checkTaskPicturesBean.setFileId(UUIDUtil.getUUID32());
                        checkTaskPicturesBean.save();
                    }

                }

            }

            boolean save = taskItemBean.save();
            numOFTaskItemBean++;
            LogUtil.i("save", "----------------TaskItemBean保存：" + save + numOFTaskItemBean);
        }

        for (RoutePosition routePosition1 : routeListBean.getRoutePositions()) {
            RoutePosition routePosition = new RoutePosition();
            routePosition.setDistance(routePosition1.getDistance());
            routePosition.setPositionLgtd(routePosition1.getPositionLgtd());
            routePosition.setPositionLttd(routePosition1.getPositionLttd());
            routePosition.setPositionId(routePosition1.getPositionId());


//            String userCode = SPUtils.getInstance().getString(CacheConsts.userCode, "");
//            String reservoirId = SPUtils.getInstance().getString(CacheConsts.reservoirId, "");
            routePosition.setUserCode(userCode);
            routePosition.setReservoirId(reservoirId);
            routePosition.setFatherId(routeListBean.getId());
            routePosition.setWorkOrderId(workOrderId);
            routePosition.setUuid(UUIDUtil.getUUID32());
            boolean save = routePosition.save();
            num++;
            LogUtil.i("save", "----------------RoutePosition保存：" + save + num);

        }
    }

}
