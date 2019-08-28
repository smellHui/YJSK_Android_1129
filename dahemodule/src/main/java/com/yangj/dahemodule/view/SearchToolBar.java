package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lxj.xpopup.XPopup;
import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/8/28
 * Description:
 */
public class SearchToolBar extends ViewBase implements SelectDataView.onDataSelectPickListener {

    private EditText dataEt;
    private SelectDataView selectDataView;

    public SearchToolBar(Context context) {
        super(context);
    }

    public SearchToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.search_toolbar;
    }

    @Override
    public void initData() {
        dataEt = findViewById(R.id.et_data);

        selectDataView = new SelectDataView(mContext, this);
        dataEt.setOnClickListener(v ->
                showChoiceDateDialog()
        );
    }

    public void showChoiceDateDialog() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(true) //启用状态栏阴影
                .dismissOnTouchOutside(false)
                .asCustom(selectDataView)
                .show();
    }

    @Override
    public void onDataSelectPickListener(String startTime, String endTime, int cate) {

    }
}
