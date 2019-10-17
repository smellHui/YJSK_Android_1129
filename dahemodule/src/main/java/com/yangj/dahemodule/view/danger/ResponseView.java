package com.yangj.dahemodule.view.danger;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.tepia.base.utils.TimeFormatUtils;
import com.tepia.base.utils.ToastUtils;
import com.tepia.guangdong_module.amainguangdong.common.PhotoSelectAdapter;
import com.tepia.guangdong_module.amainguangdong.common.pickview.PhotoRecycleViewAdapter;
import com.tepia.photo_picker.PhotoPicker;
import com.tepia.photo_picker.PhotoPreview;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.user.SysUser;
import com.yangj.dahemodule.view.ViewBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xch
 * Date:2019/10/16
 * Description:
 */
public class ResponseView extends ViewBase {

    private TextView tv_report;
    private TextView tv_createdTime;
    private EditText et_content;
    private RecyclerView rv;
    private PhotoSelectAdapter photoRecycleViewAdapterBefore;
    private ArrayList<String> selectPhotosBefore = new ArrayList<>();
    private String content;
    private List<String> imgs;

    private FeekBackListener feekBackListener;

    public void setFeekBackListener(FeekBackListener feekBackListener) {
        this.feekBackListener = feekBackListener;
    }

    public ResponseView(Context context) {
        super(context);
    }

    public ResponseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResponseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_response;
    }

    @Override
    public void initData() {

        tv_report = findViewById(R.id.tv_report);
        tv_createdTime = findViewById(R.id.tv_createdTime);
        et_content = findViewById(R.id.et_content);

        tv_createdTime.setText(TimeFormatUtils.getStringToday());

        SysUser sysUser = HttpManager.getInstance().getSysUser();
        if (sysUser != null) {
            String userName = sysUser.getUsername();
            if (!Strings.isNullOrEmpty(userName)) {
                tv_report.setVisibility(VISIBLE);
                tv_report.setText(String.format("上报人：%s", userName));
            } else {
                tv_report.setVisibility(GONE);
            }
        } else {
            tv_report.setVisibility(GONE);
        }

        findViewById(R.id.btn_pass).setOnClickListener(v -> {
            content = et_content.getText().toString().trim();
            if (Strings.isNullOrEmpty(content)) {
                ToastUtils.shortToast("反馈内容不能为空");
                return;
            }
            if (feekBackListener != null) {
                feekBackListener.feekBackClick(content, imgs);
            }
        });

        rv = findViewById(R.id.rv_add_photo_before);
        photoRecycleViewAdapterBefore = new PhotoSelectAdapter(getContext(), false);
        rv.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        rv.setAdapter(photoRecycleViewAdapterBefore);
        photoRecycleViewAdapterBefore.setOnItemClickListener((view, position) -> {
            if (photoRecycleViewAdapterBefore.getItemViewType(position) == PhotoRecycleViewAdapter.TYPE_ADD) {
                PhotoPicker.builder()
                        .setPhotoCount(5)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .setSelected(selectPhotosBefore)
                        .start((Activity) mContext, 100);
            } else {
                PhotoPreview.builder()
                        .setPhotos(photoRecycleViewAdapterBefore.getPhotoPaths())
                        .setCurrentItem(position)
                        .setShowDeleteButton(false)
                        .start((Activity) mContext, 101);
            }
        });
    }

    public void setData(ArrayList<String> imgs) {
        this.imgs = imgs;
        photoRecycleViewAdapterBefore.setLocalData(imgs);
    }

    public interface FeekBackListener {
        void feekBackClick(String content, List<String> imgs);
    }
}
