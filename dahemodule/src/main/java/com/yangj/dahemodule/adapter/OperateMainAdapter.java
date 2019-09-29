package com.yangj.dahemodule.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.model.main.Route;

import java.util.List;

/**
 * Author:xch
 * Date:2019/9/18
 * Description:
 */
public class OperateMainAdapter extends BaseQuickAdapter<Route, BaseViewHolder> {

    public OperateMainAdapter(@Nullable List<Route> data) {
        super(R.layout.item_operate_main, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Route item) {
        View bg = helper.getView(R.id.ll_one);
        ImageView cateImg = helper.getView(R.id.img_cate);
        String type = item.getType();
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("1")) {
                bg.setBackgroundResource(R.mipmap.btn_1);
                cateImg.setImageResource(R.mipmap.btn_cir_1);
            }
            if (type.equals("2")) {
                bg.setBackgroundResource(R.mipmap.btn_2);
                cateImg.setImageResource(R.mipmap.btn_cir_2);
            }
            if (type.equals("3")) {
                bg.setBackgroundResource(R.mipmap.btn_3);
                cateImg.setImageResource(R.mipmap.btn_cir_3);
            }
            if (type.equals("4")) {
                bg.setBackgroundResource(R.mipmap.btn_1);
                cateImg.setImageResource(R.mipmap.btn_cir_1);
            }
        }
        helper.setText(R.id.tv_name, item.getName());
    }
}
