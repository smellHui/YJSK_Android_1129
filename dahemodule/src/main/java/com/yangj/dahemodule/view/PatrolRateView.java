package com.yangj.dahemodule.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.dialog.loading.SimpleLoadDialog;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskManager;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.yangj.dahemodule.R;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author:xch
 * Date:2019/11/25
 * Description:
 */
public class PatrolRateView extends ViewBase {

    private TextView tv_rate;
    private TextView tv_abnormal_num;
    private Button btn_submit;

    private String workOrderId;

    public PatrolRateView(Context context) {
        super(context);
    }

    public PatrolRateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PatrolRateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_patrol_rate;
    }

    @Override
    public void initData() {
        tv_rate = findViewById(R.id.tv_rate);
        tv_abnormal_num = findViewById(R.id.tv_abnormal_num);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(v -> {
            ToastUtils.shortToast("提交");
            TaskBean taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
            if (taskBean != null) {
                if (taskBean.isHasCreated()) {

                } else {
                    createOrder(taskBean);
                }
            }
        });
    }

    public void setWorkOrderId(String workOrderId, boolean isCompleteOfTaskBean) {
        int abnormalNum = SqlManager.getInstance().getNormalTaskById(workOrderId, false);
        if (isCompleteOfTaskBean) {
            tv_rate.setText("100%");
            tv_abnormal_num.setText(String.format("%d项", abnormalNum));
            btn_submit.setVisibility(GONE);
        } else {
            this.workOrderId = workOrderId;
            int totalNum = SqlManager.getInstance().getTotalTaskById(workOrderId);
            int completedNum = SqlManager.getInstance().getTotalTaskByStatus(workOrderId, "1");
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String num = df.format((float) completedNum * 100 / totalNum);//返回的是String类型
            tv_rate.setText(num + "%");
            tv_abnormal_num.setText(String.format("%d项", abnormalNum));
            btn_submit.setVisibility(completedNum == totalNum ? VISIBLE : GONE);
        }
    }

    public void updateRateView() {
        setWorkOrderId(workOrderId, false);
    }

    private void createOrder(TaskBean taskBean) {
        TaskManager.getInstance().newStartExecute(workOrderId, taskBean.getRouteId(), taskBean.getStartTime()).safeSubscribe(new LoadingSubject<TaskDetailResponse>(true, "正在新建工单...") {

            @Override
            protected void _onNext(TaskDetailResponse taskDetailResponse) {
                if (taskDetailResponse == null) return;
                if (taskDetailResponse.getCode() != 0) return;
                taskBean.setHasCreated(true);
                SqlManager.getInstance().updateTaskAsyn(taskBean, null);
                List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryLocalData(workOrderId);
                new TaskDetailPresenter(this::SubmitFormCallback).commitTotal(taskItemBeans, mContext);
            }

            private void SubmitFormCallback() {
                String temp = RoutepointDataManager.getInstance().getRoutePointListString(workOrderId);
                new TaskDetailPresenter(this::endFormCallback).endExecute(workOrderId, temp, false, ResUtils.getString(com.example.guangdong_module.R.string.data_loading));
            }

            private void endFormCallback() {
                DataSupport.deleteAll(TaskBean.class, "workOrderId=?", workOrderId);
                DataSupport.deleteAll(TaskItemBean.class, "workOrderId=?", workOrderId);
                DataSupport.deleteAll(RouteListBean.class, "workOrderId=?", workOrderId);
                DataSupport.deleteAll(RoutePosition.class, "workOrderId=?", workOrderId);
                ToastUtils.shortToast("提交成功");
                ((Activity) mContext).finish();
            }

            @Override
            protected void _onError(String message) {

            }
        });
    }

}
