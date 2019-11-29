package com.yangj.dahemodule.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tepia.base.ConfigConsts;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity;
import com.yangj.dahemodule.activity.DangerReportDetailActivity;
import com.yangj.dahemodule.activity.PatrolMapControlActivity1;
import com.yangj.dahemodule.activity.PatrolUpControlActivity;

import java.util.ArrayList;

import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TASK_ITEM_BEAN;
import static com.tepia.guangdong_module.amainguangdong.xunchaview.activity.TroubleRecordActivity.TROUBLE_POSITION;
import static com.yangj.dahemodule.activity.PatrolUpControlActivity.IS_COMPLETE_OF_TASKBEAN;
import static com.yangj.dahemodule.activity.PatrolUpControlActivity.PATROL_INDEX;
import static com.yangj.dahemodule.activity.PatrolUpControlActivity.ROUTE_POSITIONS;

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
        Intent intent = new Intent(ctx, PatrolMapControlActivity1.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid, workOrderId);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }

    public static void goToPatrolUpControlView(Context ctx, int patrolIndex, boolean isCompleteOfTaskBean, ArrayList<RoutePosition> routePositions) {
        Intent intent = new Intent(ctx, PatrolUpControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PATROL_INDEX, patrolIndex);
        bundle.putBoolean(IS_COMPLETE_OF_TASKBEAN, isCompleteOfTaskBean);
        bundle.putSerializable(ROUTE_POSITIONS, routePositions);
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

    public static void goToTroubleRecordView(Context ctx, TaskItemBean taskItemBean, String executeResultType) {
        goToTroubleRecordView(ctx, taskItemBean, -1, executeResultType);
    }

    public static void goToTroubleRecordView(Context ctx, TaskItemBean taskItemBean, int position, String executeResultType) {
        Intent intent = new Intent(ctx, TroubleRecordActivity.class);
        intent.putExtra(TASK_ITEM_BEAN, taskItemBean);
        intent.putExtra("executeResultType", executeResultType);
        if (position != -1)
            intent.putExtra(TROUBLE_POSITION, position);
        ctx.startActivity(intent);
    }
}
