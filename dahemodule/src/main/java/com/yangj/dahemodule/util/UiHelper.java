package com.yangj.dahemodule.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tepia.base.ConfigConsts;
import com.tepia.guangdong_module.amainguangdong.xunchaview.activity.StartInspectionActivity;

/**
 * Author:xch
 * Date:2019/9/9
 * Description:
 */
public class UiHelper {

    public static void goToStartInspectionView(Context ctx,String workOrderId){
        Intent intent = new Intent();
        intent.setClass(ctx, StartInspectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConfigConsts.Workorderid,workOrderId);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }
}
