package com.yangj.dahemodule.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tepia.base.ConfigConsts;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity;
import com.yangj.dahemodule.activity.DangerReportDetailActivity;
import com.yangj.dahemodule.activity.PatrolMapControlActivity;

import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.PICKER_INDEX;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TASK_ITEM_BEAN;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TROUBLE_POSITION;

/**
 * Author:xch
 * Date:2019/9/9
 * Description:
 */
public class UiHelper {

    public static void goToStartInspectionView(Context ctx, String workOrderId) {
        Intent intent = new Intent(ctx, StartInspectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid, workOrderId);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }

    public static void goToPatrolMapControlView(Context ctx, String workOrderId) {
//        Intent intent = new Intent(ctx, PatrolMapControlActivity.class);
        Intent intent = new Intent(ctx, PatrolMapControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid, workOrderId);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }

    /**
     * 跳转险情上报详情页
     *
     * @param ctx
     * @param reportId
     */
    public static void goToDangerReportDetailView(Context ctx, String reportId) {
        Intent intent = new Intent(ctx, DangerReportDetailActivity.class);
        intent.putExtra("reportId", reportId);
        ctx.startActivity(intent);
    }

    public static void goToTroubleRecordView(Context ctx, TaskItemBean taskItemBean) {
        goToTroubleRecordView(ctx, taskItemBean, -1, -1);
    }

    public static void goToTroubleRecordView(Context ctx, TaskItemBean taskItemBean, int position, int patrolIndex) {
        Intent intent = new Intent(ctx, TroubleRecordActivity.class);
        intent.putExtra(TASK_ITEM_BEAN, taskItemBean);
        if (position != -1)
            intent.putExtra(TROUBLE_POSITION, position);
        if (patrolIndex != -1)
            intent.putExtra(PICKER_INDEX, patrolIndex);
        ctx.startActivity(intent);
    }
}
