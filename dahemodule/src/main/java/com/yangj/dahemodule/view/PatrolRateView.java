package com.yangj.dahemodule.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.utils.ResUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskDetailPresenter;
import com.tepia.guangdong_module.amainguangdong.mvp.taskdetail.TaskManager;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskDetailResponse;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.wrap.SubmitTaskWrap;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
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
            TaskBean taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
            if (taskBean != null) {
                if (taskBean.isHasCreated()) {
                    uploadForm();
                } else {
                    createOrder(taskBean);
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
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
                uploadForm();
            }


            @Override
            protected void _onError(String message) {
                ToastUtils.shortToast(message);
            }
        });
    }

    private void uploadForm() {
        List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryLocalData(workOrderId);
        if (CollectionsUtil.isEmpty(taskItemBeans)) {
            SubmitFormCallback();
        } else {
            new TaskDetailPresenter(this::SubmitFormCallback).commitTotal(taskItemBeans, mContext);
        }
    }

    private void SubmitFormCallback() {
        String temp = RoutepointDataManager.getInstance().getRoutePointListString(workOrderId);
        new TaskDetailPresenter(this::endFormCallback).endExecute(workOrderId, temp, false, ResUtils.getString(com.example.guangdong_module.R.string.data_loading));
    }

    private void endFormCallback() {
        SqlManager.getInstance().deleteTask(workOrderId);
        ToastUtils.shortToast("提交成功");
        EventBus.getDefault().post(new SubmitTaskWrap());
    }

}
