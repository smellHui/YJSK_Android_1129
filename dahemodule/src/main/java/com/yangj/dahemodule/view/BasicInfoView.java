package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:基础信息
 */
public class BasicInfoView extends ViewBase{
    public BasicInfoView(Context context) {
        super(context);
    }

    public BasicInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasicInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_basic_info;
    }

    @Override
    public void initData() {

    }
}
