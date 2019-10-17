package com.yangj.dahemodule.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/5/20
 * Description:
 */
public class ImageSeeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ImageSeeAdapter() {
        super(R.layout.item_image_see);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView img = helper.getView(R.id.img);
        Glide.with(mContext).load(item).into(img);
    }
}
