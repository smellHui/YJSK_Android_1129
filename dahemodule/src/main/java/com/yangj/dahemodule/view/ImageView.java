package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:
 */
public class ImageView extends ViewBase{
    public ImageView(Context context) {
        super(context);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_image;
    }

    @Override
    public void initData() {

    }
}
