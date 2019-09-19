package com.yangj.dahemodule.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tepia.base.ConfigConsts;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;
import com.yangj.dahemodule.activity.DangerReportDetailActivity;

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
}
